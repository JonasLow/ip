package jody;

public class JodyException extends Exception {
    public JodyException(String message) {
        super(message);
    }

    public JodyException(String message, Throwable cause) {
        super(message, cause);
    }
}
