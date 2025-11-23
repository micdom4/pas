package team.four.pas.exceptions.allocation;

public class AllocationNotActiveException extends AllocationException {
    public AllocationNotActiveException(String message) {
        super(message);
    }

    public AllocationNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
