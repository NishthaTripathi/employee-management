package com.wisetech.employee_management.repository;

import com.wisetech.employee_management.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}