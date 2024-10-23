package com.wisetech.employee_management.exception;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(String.format("%s not found with ID: %d", resourceName, resourceId), "RESOURCE_NOT_FOUND");
    }
}