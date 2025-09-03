package exception;

public class PropertyException extends Exception {
    public PropertyException(Throwable cause) {
        super(cause);
    }

    public PropertyException(String message) {
        super(message);
    }
}
