package team.four.pas.exceptions.user;

public class UserInvalidTypeException extends UserException {
    public UserInvalidTypeException(String message) {
        super(message);
    }

    public UserInvalidTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
