package BusinessLogic.Exceptions;

public class LateCancellationException extends RuntimeException {
    public LateCancellationException(String message) {
        super(message);
    }
}
