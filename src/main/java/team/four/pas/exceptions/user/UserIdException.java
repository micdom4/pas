package team.four.pas.exceptions.user;

public class UserIdException extends UserException {
    public UserIdException(String message) {
        super(message);
    }

    public UserIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
