package team.four.pas.exceptions.allocation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Inactive client cannot Allocate")
public class InactiveClientException extends AllocationException {
    public InactiveClientException(String message) {
        super(message);
    }

    public InactiveClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
