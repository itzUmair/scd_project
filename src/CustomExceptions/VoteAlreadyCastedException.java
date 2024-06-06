package CustomExceptions;

public class VoteAlreadyCastedException extends Exception {
    public VoteAlreadyCastedException (String message) {
        super(message);
    }
}
