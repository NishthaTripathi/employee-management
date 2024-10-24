package com.wisetech.employee_management.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    //TODO: doc
    boolean existsByName(String name);

    List<Department> findAllByMandatoryTrue();
}
