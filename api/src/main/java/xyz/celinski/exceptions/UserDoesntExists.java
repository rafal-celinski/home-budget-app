package xyz.celinski.exceptions;

public class UserDoesntExists extends Exception {
    public UserDoesntExists(String message) {
        super(message);
    }
}