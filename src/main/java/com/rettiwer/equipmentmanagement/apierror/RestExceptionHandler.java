package com.rettiwer.equipmentmanagement.apierror;

import com.rettiwer.equipmentmanagement.apierror.exception.InsufficientPermissionException;
import com.rettiwer.equipmentmanagement.apierror.exception.RelationConstraintViolationException;
import com.rettiwer.equipmentmanagement.user.exception.UserHasEmployeesException;
import com.rettiwer.equipmentmanagement.user.exception.UserHasItemsException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {

    /**
     * Handle AuthenticationException. Triggered when a credentials are wrong.
     */
    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        String msg = "These credentials do not match our records.";
        return buildResponseEntity(new ApiError(UNAUTHORIZED, msg, ex));
    }

    @ExceptionHandler(InsufficientPermissionException.class)
    protected ResponseEntity<Object> handleInsufficientPermissionException(Exception ex) {

        String msg = "You do not have sufficient permissions to perform " +
                "the requested action or access the requested resource.";

        return buildResponseEntity(new ApiError(FORBIDDEN, msg, ex));
    }

    @ExceptionHandler(UserHasItemsException.class)
    protected ResponseEntity<Object> handleUserHasItemsException(Exception ex) {

        String msg = "User has assigned items, reassign them to other user before deletion.";
        return buildResponseEntity(new ApiError(BAD_REQUEST, msg, ex));
    }

    @ExceptionHandler(UserHasEmployeesException.class)
    protected ResponseEntity<Object> handleUserHasEmployeesException(Exception ex) {

        String msg = "User has assigned employees, reassign them to other user before deletion.";
        return buildResponseEntity(new ApiError(BAD_REQUEST, msg, ex));
    }

    @ExceptionHandler({ MalformedJwtException.class })
    public ResponseEntity<Object> handleInvalidJWTException(Exception ex) {
        String msg = "Access token is invalid.";
        return buildResponseEntity(new ApiError(UNAUTHORIZED, msg, ex));
    }

    /**
     * Handles MethodArgumentNotValidException. Invoked when validated object fails.
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }


    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {

        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(RelationConstraintViolationException.class)
    protected ResponseEntity<Object> handleRelationConstraintViolationException(RelationConstraintViolationException ex) {
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    /**
     * Handle Exception, handle generic Exception.class
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}