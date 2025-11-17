package team.four.pas.controllers.exceptions.service;

import team.four.pas.controllers.exceptions.AppBaseException;

public class ResourceServiceException extends AppBaseException {
    public ResourceServiceException(String message) {
        super(message);
    }

    public ResourceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
