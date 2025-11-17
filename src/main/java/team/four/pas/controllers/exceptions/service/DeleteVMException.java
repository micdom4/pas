package team.four.pas.controllers.exceptions.service;

public class DeleteVMException extends RuntimeException {
    public DeleteVMException(String message) {
        super(message);
    }

    public DeleteVMException(String message, Throwable cause) {
        super(message, cause);
    }
}
