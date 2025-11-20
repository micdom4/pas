package team.four.pas.exceptions.user;

public class UserDataValidationException extends UserException {
    public UserDataValidationException(String message) {
        super(message);
    }

    public UserDataValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
