package com.wisetech.employee_management.api.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
 public class ErrorResponse {
    private String errorCode;
    private String message;
}
