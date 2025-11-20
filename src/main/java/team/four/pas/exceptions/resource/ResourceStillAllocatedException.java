package team.four.pas.exceptions.resource;

public class ResourceStillAllocatedException extends ResourceException {
    public ResourceStillAllocatedException(String message) {
        super(message);
    }

    public ResourceStillAllocatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
