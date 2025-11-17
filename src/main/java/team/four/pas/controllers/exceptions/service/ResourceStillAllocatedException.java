package team.four.pas.controllers.exceptions.service;

public class ResourceStillAllocatedException extends ResourceServiceException {
    public ResourceStillAllocatedException(String message) {
        super(message);
    }

    public ResourceStillAllocatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
