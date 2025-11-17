package team.four.pas.controllers.exceptions.service;

public class DataValidationException extends AddVMException {
    public DataValidationException(String message) {
        super(message);
    }

    public DataValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
