package team.four.pas.exceptions.user;

public class UserLoginException extends UserException {
    public UserLoginException(String message) {
        super(message);
    }

    public UserLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
