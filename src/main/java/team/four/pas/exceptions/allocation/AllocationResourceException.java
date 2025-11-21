package team.four.pas.exceptions.allocation;

public class AllocationResourceException extends AllocationException {
    public AllocationResourceException(String message) {
        super(message);
    }

    public AllocationResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
