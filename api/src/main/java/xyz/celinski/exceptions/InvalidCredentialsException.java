package xyz.celinski.exceptions;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException (String message) {
        super(message);
    }
}