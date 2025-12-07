package team.four.pas.exceptions.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Invalid resource ID")
public class ResourceIdException extends ResourceException {
    public ResourceIdException(String message) {
        super(message);
    }

    public ResourceIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
