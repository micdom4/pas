package team.four.pas.exceptions.user;

public class UserFindException extends UserException {
    public UserFindException(String message) {
        super(message);
    }

    public UserFindException(String message, Throwable cause) {
        super(message, cause);
    }
}
