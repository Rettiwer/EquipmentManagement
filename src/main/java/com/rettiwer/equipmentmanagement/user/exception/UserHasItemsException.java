package com.rettiwer.equipmentmanagement.user.exception;

public class UserHasItemsException extends RuntimeException {
    public UserHasItemsException() {
        super();
    }

    public UserHasItemsException(String message) {
        super(message);
    }

    public UserHasItemsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserHasItemsException(Throwable cause) {
        super(cause);
    }
}