package com.bipul.ems;

import com.bipul.ems.dto.EmployeeDTO;
import com.bipul.ems.exception.ResourceNotFoundException;
import com.bipul.ems.model.Employee;
import com.bipul.ems.repository.EmployeeRepository;
import com.bipul.ems.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Bipul");
        employee.setLastName("Kumar");
        employee.setEmail("bipul@test.com");
        employee.setDepartment("Engineering");
        employee.setDesignation("Backend Developer");
        employee.setSalary(50000.0);
        employee.setStatus(Employee.EmployeeStatus.ACTIVE);

        employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("Bipul");
        employeeDTO.setLastName("Kumar");
        employeeDTO.setEmail("bipul@test.com");
        employeeDTO.setDepartment("Engineering");
        employeeDTO.setDesignation("Backend Developer");
        employeeDTO.setSalary(50000.0);
    }

    @Test
    void createEmployee_Success() {
        when(employeeRepository.existsByEmail(employeeDTO.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO result = employeeService.createEmployee(employeeDTO);

        assertNotNull(result);
        assertEquals("Bipul", result.getFirstName());
        assertEquals("bipul@test.com", result.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_DuplicateEmail_ThrowsException() {
        when(employeeRepository.existsByEmail(employeeDTO.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> employeeService.createEmployee(employeeDTO));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void getAllEmployees_ReturnsList() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bipul", result.get(0).getFirstName());
    }

    @Test
    void getEmployeeById_Found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDTO result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("bipul@test.com", result.getEmail());
    }

    @Test
    void getEmployeeById_NotFound_ThrowsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(99L));
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void deleteEmployee_NotFound_ThrowsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(99L));
    }
}
