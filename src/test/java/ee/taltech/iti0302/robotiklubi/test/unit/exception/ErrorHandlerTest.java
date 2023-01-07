package ee.taltech.iti0302.robotiklubi.test.unit.exception;

import ee.taltech.iti0302.robotiklubi.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {

    private final ErrorHandler handler = new ErrorHandler();

    @Test
    void notFoundErrorTest() {
        NotFoundException exception = new NotFoundException("Error Message.");
        ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(exception);
        assertEquals("Error Message.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void badRequestErrorTest() {
        BadRequestException exception = new BadRequestException("Error Message.");
        ResponseEntity<ErrorResponse> response = handler.handleBadRequestException(exception);
        assertEquals("Error Message.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void internalServerErrorTest() {
        InternalServerException exception = new InternalServerException("Error Message.", new Exception());
        ResponseEntity<ErrorResponse> response = handler.handleInternalServerException(exception);
        assertEquals("Error Message.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void internalServerErrorWithNotFoundErrorTest() {
        InternalServerException exception = new InternalServerException("Outer Message.", new NotFoundException("Inner Message."));
        ResponseEntity<ErrorResponse> response = handler.handleInternalServerException(exception);
        assertEquals("Outer Message. Reason: Inner Message.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void internalServerErrorWithBadRequestErrorTest() {
        InternalServerException exception = new InternalServerException("Outer Message.", new BadRequestException("Inner Message."));
        ResponseEntity<ErrorResponse> response = handler.handleInternalServerException(exception);
        assertEquals("Outer Message. Reason: Inner Message.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void tokenParseErrorTest() {
        TokenParseException exception = new TokenParseException("Error Message.", new Exception());
        ResponseEntity<ErrorResponse> response = handler.handleTokenParseException(exception);
        assertEquals("Error Message.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void accessDeniedErrorTest() {
        AccessDeniedException exception = new AccessDeniedException("Error Message.");
        ResponseEntity<ErrorResponse> response = handler.handleAccessDeniedException(exception);
        assertEquals("Error Message.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void fileProcessingErrorTest() {
        FileProcessingException exception = new FileProcessingException("Error Message.");
        ResponseEntity<ErrorResponse> response = handler.handleFileProcessingException(exception);
        assertEquals("Error Message.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void genericErrorTest() {
        Exception exception = new Exception("Original Error Message.");
        ResponseEntity<ErrorResponse> response = handler.handleException(exception);
        assertEquals("Internal Error.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
