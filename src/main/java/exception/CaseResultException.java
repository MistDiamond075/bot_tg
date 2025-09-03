package exception;

public class CaseResultException extends Exception {
    public CaseResultException(Throwable cause) {
        super(cause);
    }

    public CaseResultException(String message) {
        super(message);
    }
}
