package team.four.pas.exceptions.user;

public class UserNotPresentException extends UserException {
    public UserNotPresentException(String message) {
        super(message);
    }

    public UserNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }
}
