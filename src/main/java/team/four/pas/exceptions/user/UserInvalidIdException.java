package team.four.pas.exceptions.user;

public class UserInvalidIdException extends UserException {
    public UserInvalidIdException(String message) {
        super(message);
    }

    public UserInvalidIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
