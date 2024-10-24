
package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ReadOnlyDepartmentException;
import com.wisetech.employee_management.exception.ResourceAlreadyExistsException;
import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        department = new Department();
        department.setId(1L);
        department.setName("HR");
        department.setReadOnly(false);
    }

    @Test
    public void testCreateDepartment() throws ResourceAlreadyExistsException {
        when(departmentRepository.existsByName(anyString())).thenReturn(false);

        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department createdDepartment = departmentService.createDepartment(department);

        assertNotNull(createdDepartment);
        assertEquals("HR", createdDepartment.getName());
    }

    @Test
    public void testCreateDepartment_Exception() {
        when(departmentRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> departmentService.createDepartment(department)
        );
    }

    @Test
    public void testGetAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department));

        List<Department> departments = departmentService.getAllDepartments();

        assertFalse(departments.isEmpty());
        assertEquals(1, departments.size());
    }

    @Test
    public void testGetDepartmentById_ExistingId() throws ResourceNotFoundException {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Department foundDepartment = departmentService.getDepartmentById(1L);

        assertNotNull(foundDepartment);
        assertEquals(department.getName(), foundDepartment.getName());
    }

    @Test
    public void testGetDepartmentById_NonExistingId() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.getDepartmentById(1L);
        });
    }

    @Test
    public void testUpdateDepartment_Success() throws ResourceNotFoundException, ReadOnlyDepartmentException {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));


        Department updatedDepartment = new Department();
        updatedDepartment.setId(1L);
        updatedDepartment.setName("Finance");
        updatedDepartment.setReadOnly(false);

        when(departmentRepository.save(any(Department.class))).thenReturn(updatedDepartment);

        Department result = departmentService.updateDepartment(updatedDepartment);

        assertEquals("Finance", result.getName());
    }

    @Test
    public void testUpdateDepartment_ReadOnlyException() {
        department.setReadOnly(true);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        assertThrows(ReadOnlyDepartmentException.class, () -> {
            departmentService.updateDepartment(department);
        });
    }

    @Test
    public void testDeleteDepartment_Success() throws ResourceNotFoundException, ReadOnlyDepartmentException {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        departmentService.deleteDepartment(1L);

        // Verify that the delete method is called on the repository
        verify(departmentRepository).delete(department);
    }

    @Test
    public void testDeleteDepartment_ReadOnlyException() {
        department.setReadOnly(true);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        assertThrows(ReadOnlyDepartmentException.class, () -> {
            departmentService.deleteDepartment(1L);
        });
    }

    @Test
    public void testDeleteDepartment_NonExistingId() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.deleteDepartment(1L);
        });
    }
}
