package team.four.pas.controllers.exceptions.service;

public class UpdateVMException extends ResourceServiceException {
    public UpdateVMException(String message) {
        super(message);
    }

    public UpdateVMException(String message, Throwable cause) {
        super(message, cause);
    }
}
