package team.four.pas.exceptions.user;

import team.four.pas.exceptions.AppBaseException;

public abstract class UserException extends AppBaseException {
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
