package team.four.pas.exceptions.user;

public class UserInvalidLoginException extends UserException {
    public UserInvalidLoginException(String message) {
        super(message);
    }

    public UserInvalidLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
