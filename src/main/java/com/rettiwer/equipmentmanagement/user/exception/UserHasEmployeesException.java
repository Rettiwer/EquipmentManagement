package com.rettiwer.equipmentmanagement.user.exception;

public class UserHasEmployeesException extends RuntimeException {
    public UserHasEmployeesException() {
        super();
    }

    public UserHasEmployeesException(String message) {
        super(message);
    }

    public UserHasEmployeesException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserHasEmployeesException(Throwable cause) {
        super(cause);
    }
}