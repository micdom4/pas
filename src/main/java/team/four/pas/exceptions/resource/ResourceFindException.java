package team.four.pas.exceptions.resource;

public class ResourceFindException extends ResourceException {
    public ResourceFindException(String message) {
        super(message);
    }

    public ResourceFindException(String message, Throwable cause) {
        super(message, cause);
    }
}
