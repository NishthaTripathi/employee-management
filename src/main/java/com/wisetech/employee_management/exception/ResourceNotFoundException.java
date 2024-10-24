package com.wisetech.employee_management.exception;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resourceName) {
        super(String.format("%s not found", resourceName), "RESOURCE_NOT_FOUND");
    }
}