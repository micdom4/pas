package team.four.pas.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid User data")
public class UserDataException extends UserException {
    public UserDataException(String message) {
        super(message);
    }

    public UserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
