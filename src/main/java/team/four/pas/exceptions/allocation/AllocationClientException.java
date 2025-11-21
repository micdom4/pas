package team.four.pas.exceptions.allocation;

public class AllocationClientException extends AllocationException {
    public AllocationClientException(String message) {
        super(message);
    }

    public AllocationClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
