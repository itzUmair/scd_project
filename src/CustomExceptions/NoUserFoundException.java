package CustomExceptions;

public class NoUserFoundException extends Exception {
    public NoUserFoundException (String message) {
        super(message);
    }
}
