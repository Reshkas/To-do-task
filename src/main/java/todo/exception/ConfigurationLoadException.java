package todo.exception;

public class ConfigurationLoadException extends RuntimeException {
    public ConfigurationLoadException(Exception e) {
        super(e.getMessage(), e);
    }

    public ConfigurationLoadException(String message) {
        super(message);
    }
}
