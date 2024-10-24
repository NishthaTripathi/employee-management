package com.wisetech.employee_management.exception.handler;

import com.wisetech.employee_management.exception.ReadOnlyDepartmentException;
import com.wisetech.employee_management.exception.ResourceAlreadyExistsException;
import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.exception.error.ErrorResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Handles exceptions by creating an ErrorResponse and returning a ResponseEntity.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException.
     *
     * @param ex the ResourceNotFoundException thrown
     * @return ResponseEntity with error details and 404 NOT_FOUND status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(ex.getErrorCode(), ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle ResourceAlreadyExistsException.
     *
     * @param ex the ResourceAlreadyExistsException thrown
     * @return ResponseEntity with error details and 409 CONFLICT status
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceConflict(ResourceAlreadyExistsException ex) {
        return buildErrorResponse(ex.getErrorCode(), ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Handle ReadOnlyDepartmentException.
     *
     * @param ex the ReadOnlyDepartmentException thrown
     * @return ResponseEntity with error details and 400 BAD_REQUEST status
     */
    @ExceptionHandler(ReadOnlyDepartmentException.class)
    public ResponseEntity<ErrorResponse> handleReadOnlyDepartment(ReadOnlyDepartmentException ex) {
        return buildErrorResponse(ex.getErrorCode(), ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Exceptions for Invalid Inputs
     *
     * @param ex the Exception thrown
     * @return ResponseEntity with error details and 400  status
     */

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleInvalidArgument(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        for (ObjectError error : ex.getAllErrors()) {
            errors.append(error.getDefaultMessage());
        }
        return buildErrorResponse("INVALID_INPUT", "Validation failed: " + errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Bad Request Exception
     *
     * @param ex the Exception thrown
     * @return ResponseEntity with error details and 400 BAD_REQUEST status
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex) {
        return buildErrorResponse("BAD_REQUEST", "The Request format is invalid :  " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle general Exception.
     *
     * @param ex the Exception thrown
     * @return ResponseEntity with error details and 500 INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return buildErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String errorCode, String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
