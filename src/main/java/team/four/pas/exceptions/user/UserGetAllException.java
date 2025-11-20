package team.four.pas.exceptions.user;

public class UserGetAllException extends UserException {
    public UserGetAllException(String message) {
        super(message);
    }

    public UserGetAllException(String message, Throwable cause) {
        super(message, cause);
    }
}
