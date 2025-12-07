package team.four.pas.exceptions.allocation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Allocation")
public class AllocationNotFoundException extends AllocationException {
    public AllocationNotFoundException(String message) {
        super(message);
    }

    public AllocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
