package team.four.pas.exceptions.allocation;

public class InactiveClientException extends AllocationException {
    public InactiveClientException(String message) {
        super(message);
    }

    public InactiveClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
