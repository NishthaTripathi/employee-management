package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Employee;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee) throws ResourceNotFoundException;

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id) throws ResourceNotFoundException;

    Employee updateEmployee(Employee updatedEmployee) throws ResourceNotFoundException;

    void deleteEmployee(Long id) throws ResourceNotFoundException;
}
