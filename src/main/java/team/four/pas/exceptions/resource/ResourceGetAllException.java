package team.four.pas.exceptions.resource;

public class ResourceGetAllException extends ResourceException {
    public ResourceGetAllException(String message) {
        super(message);
    }

    public ResourceGetAllException(String message, Throwable cause) {
        super(message, cause);
    }
}
