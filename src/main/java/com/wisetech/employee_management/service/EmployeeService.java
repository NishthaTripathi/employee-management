package com.wisetech.employee_management.service;

import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.Employee;
import com.wisetech.employee_management.persistence.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);

    }

    public Employee saveEmployee(Employee employee) {
        //TODO: default department as organisation
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            //TODO:Custom Exception
        }
    }

    public Employee updateEmployee(long id, Employee updatedEmployee) {
        // Query: what about Organisation if we overwrite?
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setFirstName(updatedEmployee.getFirstName());
                    employee.setLastName(updatedEmployee.getLastName());
                    employee.setDepartments(updatedEmployee.getDepartments());  
                    return employeeRepository.save(employee);
                }).orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
        //TODO: Custom Exceptions
    }
}