package team.four.pas.exceptions.resource;

public class ResourceNotPresentException extends ResourceException {
    public ResourceNotPresentException(String message) {
        super(message);
    }

    public ResourceNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }
}
