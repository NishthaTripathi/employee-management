package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ReadOnlyDepartmentException;
import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO: Move validation logic
//TODO: Logging
@Service

public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;


    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department"));
    }

    @Override
    public Department updateDepartment(Long id, Department updatedDepartment) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department"));
        if (department.getReadOnly()) {
            handleReadOnlyDepartmentUpdate(department, updatedDepartment);
        }

        department.setName(updatedDepartment.getName());
        department.setReadOnly(updatedDepartment.getReadOnly());
        department.setMandatory(updatedDepartment.getReadOnly());
        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department"));

        if (department.getReadOnly()) {
            throw new ReadOnlyDepartmentException();
        }
        departmentRepository.delete(department);
    }


    private void handleReadOnlyDepartmentUpdate(Department department, Department updatedDepartment) {
        if (updatedDepartment.getReadOnly()) {
            throw new ReadOnlyDepartmentException();
        }
    }
}