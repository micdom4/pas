package team.four.pas.exceptions.resource;

public class ResourceDataValidationException extends ResourceException {
    public ResourceDataValidationException(String message) {
        super(message);
    }

    public ResourceDataValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
