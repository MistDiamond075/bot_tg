package exception;

public class DatabaseException extends Exception {
    private String entityName;

    public DatabaseException(Throwable cause) {
        super(cause);
    }

    public DatabaseException(Throwable cause, String entityName) {
        super(cause);
        this.entityName = entityName;
    }

    public DatabaseException(String message) {
        super(message);
    }
}
