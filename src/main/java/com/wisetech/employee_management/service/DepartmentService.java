package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ReadOnlyDepartmentException;
import com.wisetech.employee_management.exception.ResourceAlreadyExistsException;
import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;

import java.util.List;


public interface DepartmentService {

    Department createDepartment(Department department) throws ResourceAlreadyExistsException;

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id) throws ResourceNotFoundException;

    Department updateDepartment(Department updatedDepartment) throws ResourceNotFoundException, ReadOnlyDepartmentException;

    void deleteDepartment(Long id) throws ResourceNotFoundException, ReadOnlyDepartmentException;
}
