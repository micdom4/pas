package team.four.pas.exceptions;

public abstract class AppBaseException extends RuntimeException {
    public AppBaseException(String message) {
        super(message);
    }

    public AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
