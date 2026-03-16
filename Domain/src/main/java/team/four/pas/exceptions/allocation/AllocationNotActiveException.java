package team.four.pas.exceptions.allocation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AllocationNotActiveException extends AllocationException {
    public AllocationNotActiveException(String message) {
        super(message);
    }

    public AllocationNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
