package ee.taltech.iti0302.robotiklubi.exception;

import lombok.Data;

@Data
public class FileProcessingException extends RuntimeException {
    private final String message;
}
