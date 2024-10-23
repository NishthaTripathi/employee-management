package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ReadOnlyDepartmentException;
import com.wisetech.employee_management.exception.ResourceAlreadyExistsException;
import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO: Move validation logic
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    //TODO: check for autowired
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
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", id));
    }

    @Override
    public Department updateDepartment(Long id, Department updatedDepartment) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
        validateUpdatedDepartment(updatedDepartment, department);
        if (department.getIsReadOnly()) {
            handleReadOnlyDepartmentUpdate(department, updatedDepartment);
        } else {
            department.setDepartmentName(updatedDepartment.getDepartmentName());
            department.setIsReadOnly(updatedDepartment.getIsReadOnly());
            department.setIsMandatory(updatedDepartment.getIsReadOnly());
        }

        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));

        if (department.getIsReadOnly()) {
            throw new ReadOnlyDepartmentException();
        }
        departmentRepository.delete(department);
    }

    private void validateUpdatedDepartment(Department updatedDepartment, Department existingDepartment) {
        if (updatedDepartment == null || updatedDepartment.getDepartmentName() == null) {
            throw new IllegalArgumentException("Department or department name cannot be null.");
        }
        if (!existingDepartment.getDepartmentName().equals(updatedDepartment.getDepartmentName()) &&
                departmentRepository.existsByDepartmentName(updatedDepartment.getDepartmentName())) {
            throw new ResourceAlreadyExistsException("Department", updatedDepartment.getDepartmentName());
        }
    }

    private void handleReadOnlyDepartmentUpdate(Department department, Department updatedDepartment) {
        if (!updatedDepartment.getIsReadOnly()) {
            if (updatedDepartment.getDepartmentName() != null || updatedDepartment.getIsMandatory() != null) {
                throw new ReadOnlyDepartmentException();
            }
            department.setIsReadOnly(false);
        } else {
            throw new ReadOnlyDepartmentException();
        }
    }
}