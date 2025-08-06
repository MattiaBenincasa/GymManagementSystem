package BusinessLogic.Exceptions;

public class LateBookingDeletionException extends RuntimeException {
    public LateBookingDeletionException(String message) {
        super(message);
    }
}
