package team.four.pas.exceptions.resource;

public class ResourceInvalidIdException extends ResourceException {
    public ResourceInvalidIdException(String message) {
        super(message);
    }

    public ResourceInvalidIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
