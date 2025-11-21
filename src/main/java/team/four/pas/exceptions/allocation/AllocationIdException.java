package team.four.pas.exceptions.allocation;

public class AllocationIdException extends AllocationException {
    public AllocationIdException(String message) {
        super(message);
    }

    public AllocationIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
