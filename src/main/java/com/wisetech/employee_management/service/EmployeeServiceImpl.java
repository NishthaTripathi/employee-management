package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.DepartmentRepository;
import com.wisetech.employee_management.persistence.Employee;
import com.wisetech.employee_management.persistence.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//TODO: Move validation logic
@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        validateDepartments(employee.getDepartments());

        List<Department> mandatoryDepartments = departmentRepository.findAllByIsMandatoryTrue();
        employee.getDepartments().addAll(mandatoryDepartments);
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
    }


    @Override
    public Employee updateEmployee(Long id, Employee updatedEmployee) {

        validateDepartments(updatedEmployee.getDepartments());

        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    Set<Department> existingMandatoryDepartments = existingEmployee.getDepartments()
                            .stream()
                            .filter(Department::getIsMandatory)
                            .collect(Collectors.toSet());

                    Set<Department> newDepartments = new HashSet<>(updatedEmployee.getDepartments());

                    newDepartments.addAll(existingMandatoryDepartments);
                    existingEmployee.setDepartments(newDepartments);
                    existingEmployee.setFirstName(updatedEmployee.getFirstName());
                    existingEmployee.setLastName(updatedEmployee.getLastName());

                    return employeeRepository.save(existingEmployee);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee", id);
        }
        employeeRepository.deleteById(id);
    }

    private void validateDepartments(Set<Department> departments) {
        for (Department department : departments) {
            if (!departmentRepository.existsById(department.getDepartmentId())) {
                throw new ResourceNotFoundException("Department", department.getDepartmentId());
            }
        }
    }
}