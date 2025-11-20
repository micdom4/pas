package team.four.pas.exceptions.user;

public class UserUpdateException extends UserException {
    public UserUpdateException(String message) {
        super(message);
    }

    public UserUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
