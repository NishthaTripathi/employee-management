package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ReadOnlyDepartmentException;
import com.wisetech.employee_management.exception.ResourceAlreadyExistsException;
import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.DepartmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    public static final long DEPARTMENT_NEW_ID = 2L;
    public static final long DEPARTMENT_EXISTING_ID = 1L;
    public static final long DEPARTMENT_NON_EXISTING_ID = 999L;
    public static final String DEPARTMENT_EXISTING_NAME = "Marketing";
    public static final String DEPARTMENT_NEW_NAME = "IT";
    public static final String DEPARTMENT_UPDATED_NAME = "HR";


    @Mock
    private DepartmentRepository mockDepartmentRepository;

    private DepartmentService ref;

    @InjectMocks
    private DepartmentServiceImpl concreteRef;

    private Department existingDepartment;
    private Department readOnlyDepartment;
    private Department newDepartment;
    private Department updatedDepartment;

    @BeforeEach
    public void setUp() {
        ref = concreteRef;

        existingDepartment = Department.builder()
                .id(DEPARTMENT_EXISTING_ID)
                .name(DEPARTMENT_EXISTING_NAME)
                .readOnly(false)
                .mandatory(false)
                .employees(new HashSet<>())
                .build();

        readOnlyDepartment = Department.builder()
                .id(DEPARTMENT_EXISTING_ID)
                .name(DEPARTMENT_EXISTING_NAME)
                .readOnly(true)
                .mandatory(false)
                .employees(new HashSet<>())
                .build();

        newDepartment = Department.builder().id(DEPARTMENT_NEW_ID)
                .name(DEPARTMENT_NEW_NAME)
                .readOnly(false)
                .mandatory(false)
                .build();

        updatedDepartment
                = Department.builder()
                .id(DEPARTMENT_EXISTING_ID)
                .name(DEPARTMENT_UPDATED_NAME)
                .readOnly(true)
                .build();

        lenient().when(mockDepartmentRepository.existsByName(DEPARTMENT_EXISTING_NAME)).thenReturn(true);
        lenient().when(mockDepartmentRepository.findById(DEPARTMENT_EXISTING_ID)).thenReturn(Optional.of(existingDepartment));
        lenient().when(mockDepartmentRepository.findById(DEPARTMENT_NON_EXISTING_ID)).thenReturn(Optional.empty());
        lenient().when(mockDepartmentRepository.save(newDepartment)).thenReturn(newDepartment);
        lenient().when(mockDepartmentRepository.save(updatedDepartment)).thenReturn(updatedDepartment);
    }

    @AfterEach
    void teardown() {
        Mockito.reset(mockDepartmentRepository);
        existingDepartment = null;
        readOnlyDepartment = null;
        newDepartment = null;
        updatedDepartment = null;
    }

    @Test
    public void testCreateDepartment_withExistingDepartment_failure() {
        newDepartment = Department.builder()
                .name(DEPARTMENT_EXISTING_NAME)
                .build();
        assertThrows(ResourceAlreadyExistsException.class, () -> ref.createDepartment(newDepartment));
    }

    @Test
    public void testCreateDepartment_withNewDepartment_success() throws ResourceAlreadyExistsException {
        Department createdDepartment = ref.createDepartment(newDepartment);
        assertEquals(DEPARTMENT_NEW_NAME, createdDepartment.getName());
        verify(mockDepartmentRepository).save(newDepartment);
    }

    @Test
    public void testGetDepartmentById_idExists_success() {
        assertDoesNotThrow(() -> ref.getDepartmentById(DEPARTMENT_EXISTING_ID));
    }

    @Test
    public void testGetDepartmentById_idDoesNotExist_failure() {
        assertThrows(ResourceNotFoundException.class, () -> ref.getDepartmentById(DEPARTMENT_NON_EXISTING_ID));
    }

    @Test
    public void testUpdateDepartment_existingReadOnlyTrue_withNewReadOnlyTrue_failure() {
        when(mockDepartmentRepository.findById(DEPARTMENT_EXISTING_ID)).thenReturn(Optional.of(readOnlyDepartment));
        updatedDepartment.setReadOnly(true);
        assertThrows(ReadOnlyDepartmentException.class, () -> ref.updateDepartment(updatedDepartment));
    }

    @Test
    public void testUpdateDepartment_existingId_withReadOnlyFalse_success() {
        assertDoesNotThrow(() -> ref.updateDepartment(updatedDepartment));
    }

    @Test
    public void testUpdateDepartment_nonExistingId_failure() {
        updatedDepartment.setId(DEPARTMENT_NON_EXISTING_ID);
        assertThrows(ResourceNotFoundException.class, () -> ref.updateDepartment(updatedDepartment));
    }

    @Test
    public void testDeleteDepartment_existingReadOnlyTrue_withNewReadOnlyTrue_failure() {
        when(mockDepartmentRepository.findById(DEPARTMENT_EXISTING_ID)).thenReturn(Optional.of(readOnlyDepartment));
        assertThrows(ReadOnlyDepartmentException.class, () -> ref.deleteDepartment(DEPARTMENT_EXISTING_ID));
    }

    @Test
    public void testDeleteDepartment_existingReadOnlyTrue_withNewReadOnlyFalse_success() {
        assertDoesNotThrow(() -> ref.deleteDepartment(DEPARTMENT_EXISTING_ID));
        verify(mockDepartmentRepository).delete(existingDepartment);
    }

    @Test
    public void testDeleteDepartment_nonExistingId_failure() {
        assertThrows(ResourceNotFoundException.class, () -> ref.deleteDepartment(DEPARTMENT_NON_EXISTING_ID));
    }

    @Test
    public void testGetAllDepartments_success() {
        List<Department> departments = List.of(existingDepartment);
        when(mockDepartmentRepository.findAll()).thenReturn(departments);
        ref.getAllDepartments();
        verify(mockDepartmentRepository).findAll();
    }
}
