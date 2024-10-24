package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ReadOnlyDepartmentException;
import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDepartment() {
        Department department = new Department();
        department.setName("HR");

        when(departmentRepository.save(department)).thenReturn(department);

        Department createdDepartment = departmentService.createDepartment(department);

        assertEquals("HR", createdDepartment.getName());
    }

    @Test
    void testGetAllDepartments() {
        Department department1 = new Department();
        department1.setName("HR");
        Department department2 = new Department();
        department2.setName("IT");

        when(departmentRepository.findAll()).thenReturn(List.of(department1, department2));

        List<Department> departments = departmentService.getAllDepartments();

        assertEquals(2, departments.size());
        assertEquals("HR", departments.get(0).getName());
        assertEquals("IT", departments.get(1).getName());
    }

    @Test
    void testGetDepartmentById_Success() {
        Department department = new Department();
        department.setName("HR");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Department foundDepartment = departmentService.getDepartmentById(1L);

        assertEquals("HR", foundDepartment.getName());
    }

    @Test
    void testGetDepartmentById_NotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.getDepartmentById(1L));
    }

    @Test
    void testUpdateDepartment_Success() {
        Department existingDepartment = new Department();
        existingDepartment.setName("HR");
        existingDepartment.setReadOnly(false);

        Department updatedDepartment = new Department();
        updatedDepartment.setName("Finance");
        updatedDepartment.setReadOnly(false);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.save(existingDepartment)).thenReturn(existingDepartment);
        when(departmentRepository.existsByName("Finance")).thenReturn(false);

        Department result = departmentService.updateDepartment(1L, updatedDepartment);

        assertEquals("Finance", result.getName());
        assertEquals(false, result.getReadOnly());

    }

    @Test
    void testUpdateDepartment_ReadOnlyUpdateSuccess() {
        Department existingDepartment = new Department();
        existingDepartment.setName("HR");
        existingDepartment.setReadOnly(true);

        Department updatedDepartment = new Department();
        updatedDepartment.setReadOnly(false); // Update to non-read-only

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.save(any())).thenReturn(updatedDepartment);

        Department result = departmentService.updateDepartment(1L, updatedDepartment);
        assertEquals(false, result.getReadOnly());
    }

    @Test
    void testUpdateDepartment_ReadOnlyException() {
        Department existingDepartment = new Department();
        existingDepartment.setReadOnly(true);
        existingDepartment.setName("Hr");

        Department updatedDepartment = new Department();
        updatedDepartment.setReadOnly(true);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existingDepartment));

        assertThrows(ReadOnlyDepartmentException.class, () -> departmentService.updateDepartment(1L, updatedDepartment));
    }

    @Test
    void testDeleteDepartment_Success() {
        Department department = new Department();
        department.setReadOnly(false);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).findById(1L);
        verify(departmentRepository, times(1)).delete(department);
    }

    @Test
    void testDeleteDepartment_ReadOnlyException() {
        Department department = new Department();
        department.setReadOnly(true);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        assertThrows(ReadOnlyDepartmentException.class, () -> departmentService.deleteDepartment(1L));
    }

}