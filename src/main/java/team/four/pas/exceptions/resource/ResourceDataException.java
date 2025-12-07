package team.four.pas.exceptions.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid resource data")
public class ResourceDataException extends ResourceException {
    public ResourceDataException(String message) {
        super(message);
    }

    public ResourceDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
