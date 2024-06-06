package CustomExceptions;

// This error represents no user was found!

public class NoUserFoundException extends Exception {
    public NoUserFoundException (String message) {
        super(message);
    }
}
