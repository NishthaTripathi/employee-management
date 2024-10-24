package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ReadOnlyDepartmentException;
import com.wisetech.employee_management.exception.ResourceAlreadyExistsException;
import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(Department department) throws ResourceAlreadyExistsException {
        if (departmentRepository.existsByName(department.getName())) {
            throw new ResourceAlreadyExistsException("Department",department.getName());
        }
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) throws ResourceNotFoundException {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found, id:" + id));
    }

    @Override
    public Department updateDepartment(Department updatedDepartment) throws ResourceNotFoundException, ReadOnlyDepartmentException {
        Department department = departmentRepository.findById(updatedDepartment.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found, id: " + updatedDepartment.getId()));

        if (department.getReadOnly() && updatedDepartment.getReadOnly()) {
            throw new ReadOnlyDepartmentException();
        }
        return departmentRepository.save(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) throws ResourceNotFoundException, ReadOnlyDepartmentException {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found, id: " + id));

        if (department.getReadOnly()) {
            throw new ReadOnlyDepartmentException();
        }
        departmentRepository.delete(department);
    }
}