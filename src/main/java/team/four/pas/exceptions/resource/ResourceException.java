package team.four.pas.exceptions.resource;

import team.four.pas.exceptions.AppBaseException;

public abstract class ResourceException extends AppBaseException {
    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
