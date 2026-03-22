package team.four.pas.exceptions.allocation;

import team.four.pas.exceptions.AppBaseException;

public abstract class AllocationException extends AppBaseException {
    public AllocationException(String message) {
        super(message);
    }

    public AllocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
