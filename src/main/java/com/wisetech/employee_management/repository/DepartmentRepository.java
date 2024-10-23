package com.wisetech.employee_management.repository;

import com.wisetech.employee_management.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
