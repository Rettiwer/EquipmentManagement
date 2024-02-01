package com.rettiwer.equipmentmanagement.apierror.exception;

public class RelationConstraintViolationException extends RuntimeException {
    public RelationConstraintViolationException() {
    }

    public RelationConstraintViolationException(String message) {
        super(message);
    }

    public RelationConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RelationConstraintViolationException(Throwable cause) {
        super(cause);
    }
}
