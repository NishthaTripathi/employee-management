package com.wisetech.employee_management.exception;

public class ResourceAlreadyExistsException extends BaseException {

    public ResourceAlreadyExistsException(String resourceName, String resourceId) {
        super(String.format("%s Resource already exists. ID : %s", resourceName, resourceId), "RESOURCE_ALREADY_EXISTS");
    }
}
