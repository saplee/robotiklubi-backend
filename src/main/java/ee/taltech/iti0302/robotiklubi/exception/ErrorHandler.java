package ee.taltech.iti0302.robotiklubi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException e) {
        String longMessage = e.getMessage() + " Reason: " + e.getCause().getMessage();
        log.error(longMessage, e.getCause());
        if (e.getCause() instanceof NotFoundException) {
            return new ResponseEntity<>(new ErrorResponse(longMessage), HttpStatus.NOT_FOUND);
        }
        if (e.getCause() instanceof BadRequestException) {
            return new ResponseEntity<>(new ErrorResponse(longMessage), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TokenParseException.class)
    public ResponseEntity<ErrorResponse> handleTokenParseException(TokenParseException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ErrorResponse> handleFileProcessingException(FileProcessingException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("An unknown error has occurred.", e);
        return new ResponseEntity<>(new ErrorResponse("Internal Error."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
