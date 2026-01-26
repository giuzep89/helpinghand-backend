package com.giuzep89.helpinghandbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(RecordNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorized(UnauthorizedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(value = InvalidFileException.class)
    public ResponseEntity<Object> handleInvalidFile(InvalidFileException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(value = MultipartException.class)
    public ResponseEntity<Object> handleFileUploadException(MultipartException exception) {
        // File size limit caught in 2 ways: either when reading content-length header before upload
        // or during streaming
        boolean isSizeExceeded = exception instanceof MaxUploadSizeExceededException
                || exception.getCause() instanceof IllegalStateException;

        if (isSizeExceeded) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("File size exceeds maximum allowed size of 5MB");
        }

        // Other file upload errors (corrupted file, missing part, etc.)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error processing file upload");
    }
}
