package com.wisetech.employee_management.service;

import com.wisetech.employee_management.persistence.Department;

import java.util.List;

// TODO: JAVADOC
public interface DepartmentService {

    Department createDepartment(Department department);

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    Department updateDepartment(Long id, Department updatedDepartment);

    void deleteDepartment(Long id);
}
