package Exceptions;

public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException(String message) {
        super(message);
    }
}
