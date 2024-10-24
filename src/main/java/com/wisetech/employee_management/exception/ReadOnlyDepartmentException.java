package com.wisetech.employee_management.exception;

 public class ReadOnlyDepartmentException extends BaseException {

     public ReadOnlyDepartmentException() {
        super("This department is read-only and cannot be updated or deleted.", "READ_ONLY_DEPARTMENT");
    }
}

