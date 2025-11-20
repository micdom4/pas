package team.four.pas.exceptions.resource;

public class ResourceUpdateException extends ResourceException {
    public ResourceUpdateException(String message) {
        super(message);
    }

    public ResourceUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
