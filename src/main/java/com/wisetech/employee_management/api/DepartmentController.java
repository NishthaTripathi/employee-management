package com.wisetech.employee_management.api;

import com.wisetech.employee_management.exception.ReadOnlyDepartmentException;
import com.wisetech.employee_management.exception.ResourceAlreadyExistsException;
import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(@Autowired DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) throws ResourceNotFoundException {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) throws ResourceAlreadyExistsException {

        Department createdDepartment = departmentService.createDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    @PutMapping
    public ResponseEntity<Department> updateDepartment(@RequestBody @Valid Department updatedDepartment) throws ReadOnlyDepartmentException, ResourceNotFoundException {
        Department department = departmentService.updateDepartment(updatedDepartment);
        return ResponseEntity.ok(department);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) throws ReadOnlyDepartmentException, ResourceNotFoundException {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }
}