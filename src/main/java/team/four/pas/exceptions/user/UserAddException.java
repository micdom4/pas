package team.four.pas.exceptions.user;

public class UserAddException extends UserException {
    public UserAddException(String message) {
        super(message);
    }

    public UserAddException(String message, Throwable cause) {
        super(message, cause);
    }
}
