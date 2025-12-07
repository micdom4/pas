package team.four.pas.exceptions.user;

public class UserDataException extends UserException {
    public UserDataException(String message) {
        super(message);
    }

    public UserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
