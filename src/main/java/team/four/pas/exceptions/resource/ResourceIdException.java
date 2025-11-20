package team.four.pas.exceptions.resource;

public class ResourceIdException extends ResourceException {
    public ResourceIdException(String message) {
        super(message);
    }

    public ResourceIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
