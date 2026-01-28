package team.four.pas.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Invalid User ID")
public class UserIdException extends UserException {
    public UserIdException(String message) {
        super(message);
    }

    public UserIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
