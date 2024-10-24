package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.DepartmentRepository;
import com.wisetech.employee_management.persistence.Employee;
import com.wisetech.employee_management.persistence.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;


    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) throws ResourceNotFoundException {
        validateIfDepartmentsExist(employee.getDepartments());

        List<Department> mandatoryDepartments = departmentRepository.findAllByMandatoryTrue();
        employee.getDepartments().addAll(mandatoryDepartments);
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) throws ResourceNotFoundException {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id:" + id + " does not exist"));
    }


    @Override
    public Employee updateEmployee(Employee updatedEmployee) throws ResourceNotFoundException {
        validateIfDepartmentsExist(updatedEmployee.getDepartments());

        Employee existingEmployee = employeeRepository.findById(updatedEmployee.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id: " + updatedEmployee.getId() + " does not exist"));

        Set<Department> newDepartments = new HashSet<>(updatedEmployee.getDepartments());
        newDepartments.addAll(getMandatoryDepartments(existingEmployee));
        existingEmployee.setDepartments(newDepartments);
        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long id) throws ResourceNotFoundException {
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

    private void validateIfDepartmentsExist(Set<Department> departments) throws ResourceNotFoundException {

        Set<String> departmentNames = departments.stream()
                .map(Department::getName)
                .collect(Collectors.toSet());

        Set<String> existingNames = departmentRepository.findExistingDepartmentNames(departmentNames);

        Set<String> missingDepartments = departmentNames.stream()
                .filter(name -> !existingNames.contains(name))
                .collect(Collectors.toSet());

        if (!missingDepartments.isEmpty()) {
            throw new ResourceNotFoundException("Departments with names: " + String.join(", ", missingDepartments) + " do not exist");
        }
    }
}