package team.four.pas.exceptions.resource;

public class ResourceAddException extends ResourceException {
    public ResourceAddException(String message) {
        super(message);
    }

    public ResourceAddException(String message, Throwable cause) {
        super(message, cause);
    }
}
