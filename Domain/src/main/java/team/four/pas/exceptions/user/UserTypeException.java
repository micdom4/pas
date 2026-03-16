package team.four.pas.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Invalid User Type")
public class UserTypeException extends UserException {
    public UserTypeException(String message) {
        super(message);
    }

    public UserTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
