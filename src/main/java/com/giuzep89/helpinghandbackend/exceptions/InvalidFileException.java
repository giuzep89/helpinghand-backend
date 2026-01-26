package com.giuzep89.helpinghandbackend.exceptions;

public class InvalidFileException extends RuntimeException {

    public InvalidFileException() {
        super("Invalid file");
    }

    public InvalidFileException(String message) {
        super(message);
    }
}
