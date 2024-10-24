package com.wisetech.employee_management.exception.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
 public class ErrorResponse {
    private String errorCode;
    private String message;
}
