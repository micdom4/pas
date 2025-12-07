package team.four.pas.exceptions.allocation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Invalid allocation ID")
public class AllocationIdException extends AllocationException {
    public AllocationIdException(String message) {
        super(message);
    }

    public AllocationIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
