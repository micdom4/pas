package team.four.pas.exceptions.allocation;

public class AllocationNotFoundException extends AllocationException {
    public AllocationNotFoundException(String message) {
        super(message);
    }

    public AllocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
