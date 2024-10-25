package com.wisetech.employee_management.api;

import com.wisetech.employee_management.exception.BaseException;
import com.wisetech.employee_management.exception.ErrorCode;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String EXCEPTION = "Exception: {} - {}";
    public static final String VALIDATION_FAILED = "Validation failed: ";

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BaseException ex) {
        log.error(EXCEPTION, ex.getErrorCode(), ex.getMessage(), ex);
        return buildErrorResponse(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BadRequestException.class, ValidationException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex) {
        String message = (ex instanceof MethodArgumentNotValidException)
                ? handleValidationException((MethodArgumentNotValidException) ex)
                : ex.getMessage();

        log.error(EXCEPTION, ErrorCode.BAD_REQUEST.name(), message, ex);
        return buildErrorResponse(ErrorCode.BAD_REQUEST.name(), message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error(EXCEPTION, ErrorCode.INTERNAL_SERVER_ERROR.name(), ex.getMessage(), ex);
        return buildErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
    }

    private String handleValidationException(MethodArgumentNotValidException ex) {
        return VALIDATION_FAILED + ex.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String errorCode, String message) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        return ResponseEntity.status(ErrorCode.valueOf(errorCode).getHttpStatus()).body(errorResponse);
    }
}
