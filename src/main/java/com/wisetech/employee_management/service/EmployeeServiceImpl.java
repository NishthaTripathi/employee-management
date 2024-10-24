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

        List<Department> mandatoryDepartments = departmentRepository.findAllByMandatoryTrue();
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
                .orElseThrow(() -> new ResourceNotFoundException("Employee"));
    }


    @Override
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        validateDepartments(updatedEmployee.getDepartments());

        return employeeRepository.findById(id)
                .map(existingEmployee -> {

                    Set<Department> newDepartments = new HashSet<>(updatedEmployee.getDepartments());
                    newDepartments.addAll(getMandatoryDepartments(existingEmployee));
                    existingEmployee.setDepartments(newDepartments);
                    existingEmployee.setFirstName(updatedEmployee.getFirstName());
                    existingEmployee.setLastName(updatedEmployee.getLastName());

                    return employeeRepository.save(existingEmployee);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id :" + id + "does not exists"));
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee with id :" + id + "does not exists");
        }
        employeeRepository.deleteById(id);
    }

    private Set<Department> getMandatoryDepartments(Employee employee) {
        return employee.getDepartments()
                .stream()
                .filter(Department::getMandatory)
                .collect(Collectors.toSet());
    }

    private void validateDepartments(Set<Department> departments) {
        for (Department department : departments) {
            if (!departmentRepository.existsByName(department.getName()))
                throw new ResourceNotFoundException("Department  with name: " + department.getName() + " does not exists");
        }
    }
}