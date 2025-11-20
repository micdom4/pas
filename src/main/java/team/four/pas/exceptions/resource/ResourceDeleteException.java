package team.four.pas.exceptions.resource;

public class ResourceDeleteException extends ResourceException {
    public ResourceDeleteException(String message) {
        super(message);
    }

    public ResourceDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
