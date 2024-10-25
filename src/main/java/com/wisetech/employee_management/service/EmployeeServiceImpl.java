package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.DepartmentRepository;
import com.wisetech.employee_management.persistence.Employee;
import com.wisetech.employee_management.persistence.EmployeeRepository;
import org.springframework.stereotype.Service;

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

        Set<Department> existingMandatoryDepartments = getMandatoryDepartments(existingEmployee);
        updatedEmployee.getDepartments().addAll(existingMandatoryDepartments);

        return employeeRepository.save(updatedEmployee);
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
        if (departments.isEmpty()) {
            return;
        }
        List<Long> departmentIds = departments.stream()
                .map(Department::getId)
                .collect(Collectors.toList());

        List<Department> existingDepartments = departmentRepository.findAllById(departmentIds);
        if (!(existingDepartments.size() == departmentIds.size())) {
            throw new ResourceNotFoundException("One or more departments do not exist");
        }
    }
}