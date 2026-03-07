package team.four.pas.exceptions.allocation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource is already allocated")
public class ResourceAlreadyAllocatedException extends AllocationException {
    public ResourceAlreadyAllocatedException(String message) {
        super(message);
    }

    public ResourceAlreadyAllocatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
