package team.four.pas.exceptions.allocation;

public class ResourceAlreadyAllocatedException extends AllocationException {
    public ResourceAlreadyAllocatedException(String message) {
        super(message);
    }

    public ResourceAlreadyAllocatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
