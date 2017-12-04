package todo.exception;

public class DaoException extends RuntimeException {
    public DaoException(Exception e) {
        super(e.getMessage(), e);
    }

    public DaoException(String message) {
        super(message);
    }
}
