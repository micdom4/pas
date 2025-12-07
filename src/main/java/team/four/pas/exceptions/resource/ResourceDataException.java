package team.four.pas.exceptions.resource;

public class ResourceDataException extends ResourceException {
    public ResourceDataException(String message) {
        super(message);
    }

    public ResourceDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
