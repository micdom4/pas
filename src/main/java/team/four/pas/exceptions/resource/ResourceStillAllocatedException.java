package team.four.pas.exceptions.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource is still allocated")
public class ResourceStillAllocatedException extends ResourceException {
    public ResourceStillAllocatedException(String message) {
        super(message);
    }

    public ResourceStillAllocatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
