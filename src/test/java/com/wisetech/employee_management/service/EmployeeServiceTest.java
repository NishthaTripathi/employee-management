package com.wisetech.employee_management.service;

import com.wisetech.employee_management.exception.ResourceNotFoundException;
import com.wisetech.employee_management.persistence.Department;
import com.wisetech.employee_management.persistence.DepartmentRepository;
import com.wisetech.employee_management.persistence.Employee;
import com.wisetech.employee_management.persistence.EmployeeRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    private static final long EMPLOYEE_EXISTING_ID = 1L;
    private static final long EMPLOYEE_NON_EXISTING_ID = 999L;
    private static final String DEPARTMENT_MANDATORY_NAME = "Organization";
    private static final long DEPARTMENT_MANDATORY_ID = 10L;

    @Mock
    private EmployeeRepository mockEmployeeRepository;

    @Mock
    private DepartmentRepository mockDepartmentRepository;

    private EmployeeService ref;

    @InjectMocks
    private EmployeeServiceImpl concreteRef;

    private Employee existingEmployee;
    private Employee newEmployee;
    private Department existingDepartment;
    private Department mandatoryDepartment;
    private Department nonExistingDepartment;


    @BeforeEach
    public void setUp() {
        ref = concreteRef;

        existingDepartment = Department.builder()
                .id(DepartmentServiceTest.DEPARTMENT_EXISTING_ID)
                .mandatory(false)
                .build();

        nonExistingDepartment = Department.builder()
                .id(DepartmentServiceTest.DEPARTMENT_NON_EXISTING_ID)
                .mandatory(false)
                .build();

        mandatoryDepartment = Department.builder()
                .name(DEPARTMENT_MANDATORY_NAME)
                .id(DEPARTMENT_MANDATORY_ID)
                .mandatory(true)
                .build();

        existingEmployee = Employee.builder()
                .id(EMPLOYEE_EXISTING_ID)
                .nameFirst("John")
                .nameLast("Doe")
                .departments(new HashSet<>(Set.of(existingDepartment)))
                .build();

        newEmployee = Employee.builder()
                .nameFirst("Jane")
                .nameLast("Smith")
                .departments(new HashSet<>(Set.of(existingDepartment)))
                .build();

        lenient().when(mockDepartmentRepository.findAllByMandatoryTrue()).thenReturn(List.of(mandatoryDepartment));
        lenient().when(mockEmployeeRepository.findAll()).thenReturn(List.of(existingEmployee));
        lenient().when(mockEmployeeRepository.save(newEmployee)).thenReturn(newEmployee);
        lenient().when(mockEmployeeRepository.findById(EMPLOYEE_EXISTING_ID)).thenReturn(Optional.of(existingEmployee));
        lenient().when(mockEmployeeRepository.save(existingEmployee)).thenReturn(existingEmployee);
        lenient().when(mockEmployeeRepository.findById(EMPLOYEE_NON_EXISTING_ID)).thenReturn(Optional.empty());
        lenient().when(mockEmployeeRepository.existsById(EMPLOYEE_NON_EXISTING_ID)).thenReturn(false);
        lenient().when(mockEmployeeRepository.existsById(EMPLOYEE_EXISTING_ID)).thenReturn(true);
    }


    @AfterEach
    void teardown() {
        Mockito.reset(mockDepartmentRepository, mockEmployeeRepository);
        existingDepartment = null;
        nonExistingDepartment = null;
        mandatoryDepartment = null;
        existingEmployee = null;
        newEmployee = null;
    }

    @Test
    public void testCreateEmployee_departmentsExist_withMandatoryDepartments_success() {
        when(mockDepartmentRepository.findAllById(List.of(DepartmentServiceTest.DEPARTMENT_EXISTING_ID))).thenReturn(List.of(existingDepartment));
        assertDoesNotThrow(() -> ref.createEmployee(newEmployee));
        verify(mockEmployeeRepository).save(newEmployee);
    }

    @Test
    public void testCreateEmployee_departmentsNotExist_failure() {
        newEmployee.setDepartments((new HashSet<>(Set.of(nonExistingDepartment))));
        assertThrows(ResourceNotFoundException.class, () -> ref.createEmployee(newEmployee));
    }


    @Test
    public void testGetAllEmployees_success() {
        ref.getAllEmployees();
        verify(mockEmployeeRepository).findAll();
    }

    @Test
    public void testGetEmployeeById_idExists_success() {
        assertDoesNotThrow(() -> ref.getEmployeeById(EMPLOYEE_EXISTING_ID));
        verify(mockEmployeeRepository).findById(EMPLOYEE_EXISTING_ID);
    }

    @Test
    public void testGetEmployeeById_idNotExist_failure() {
        assertThrows(ResourceNotFoundException.class, () -> ref.getEmployeeById(EMPLOYEE_NON_EXISTING_ID));
    }

    @Test
    public void testUpdateEmployee_idExists_withMandatoryDepartments_success() {
        when(mockDepartmentRepository.findAllById(List.of(DepartmentServiceTest.DEPARTMENT_EXISTING_ID))).thenReturn(List.of(existingDepartment));
        assertDoesNotThrow(() -> ref.updateEmployee(existingEmployee));
        verify(mockEmployeeRepository).save(existingEmployee);
    }

    @Test
    public void testUpdateEmployee_employeeNotExist_failure() {
        assertThrows(ResourceNotFoundException.class, () -> ref.updateEmployee(newEmployee));
    }

    @Test
    public void testUpdateEmployee_employeeExists_departmentNotExists_failure() {
        assertThrows(ResourceNotFoundException.class, () -> ref.updateEmployee(newEmployee));
    }

    @Test
    public void testDeleteEmployee_idExists_success() {
        assertDoesNotThrow(() -> ref.deleteEmployee(EMPLOYEE_EXISTING_ID));
        verify(mockEmployeeRepository).deleteById(EMPLOYEE_EXISTING_ID);
    }

    @Test
    public void testDeleteEmployee_idNotExists_failure() {
        assertThrows(ResourceNotFoundException.class, () -> ref.deleteEmployee(EMPLOYEE_NON_EXISTING_ID));
        verify(mockEmployeeRepository, never()).deleteById(anyLong());
    }
}

