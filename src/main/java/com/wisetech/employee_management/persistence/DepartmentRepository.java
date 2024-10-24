package com.wisetech.employee_management.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Boolean existsByName(String name);

    @Query("SELECT d.name FROM Department d WHERE d.name IN :names")
    Set<String> findExistingDepartmentNames(@Param("names") Set<String> names);

    List<Department> findAllByMandatoryTrue();
}
