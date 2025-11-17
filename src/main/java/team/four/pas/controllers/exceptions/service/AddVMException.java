package team.four.pas.controllers.exceptions.service;

public class AddVMException extends ResourceServiceException {
    public AddVMException(String message) {
        super(message);
    }

    public AddVMException(String message, Throwable cause) {
        super(message, cause);
    }
}
