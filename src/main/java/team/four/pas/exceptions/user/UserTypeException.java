package team.four.pas.exceptions.user;

public class UserTypeException extends UserException {
    public UserTypeException(String message) {
        super(message);
    }

    public UserTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
