package com.bipul.ems.service;

import com.bipul.ems.dto.EmployeeDTO;
import com.bipul.ems.exception.ResourceNotFoundException;
import com.bipul.ems.model.Employee;
import com.bipul.ems.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // CREATE
    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        log.debug("Creating new employee with email: {}", dto.getEmail());

        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Employee with email " + dto.getEmail() + " already exists");
        }

        Employee employee = mapToEntity(dto);
        Employee saved = employeeRepository.save(employee);
        log.info("Employee created with ID: {}", saved.getId());
        return mapToDTO(saved);
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        log.debug("Fetching all employees");
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // READ ONE
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        log.debug("Fetching employee with ID: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return mapToDTO(employee);
    }

    // UPDATE
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        log.debug("Updating employee with ID: {}", id);

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        // Check email conflict with another employee
        if (!existing.getEmail().equals(dto.getEmail()) &&
                employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email " + dto.getEmail() + " is already in use");
        }

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setDepartment(dto.getDepartment());
        existing.setDesignation(dto.getDesignation());
        existing.setSalary(dto.getSalary());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setJoiningDate(dto.getJoiningDate());
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }

        Employee updated = employeeRepository.save(existing);
        log.info("Employee updated with ID: {}", updated.getId());
        return mapToDTO(updated);
    }

    // DELETE
    public void deleteEmployee(Long id) {
        log.debug("Deleting employee with ID: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        employeeRepository.delete(employee);
        log.info("Employee deleted with ID: {}", id);
    }

    // SEARCH
    @Transactional(readOnly = true)
    public List<EmployeeDTO> searchEmployees(String keyword) {
        log.debug("Searching employees with keyword: {}", keyword);
        return employeeRepository.searchEmployees(keyword)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // BY DEPARTMENT
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ALL DEPARTMENTS
    @Transactional(readOnly = true)
    public List<String> getAllDepartments() {
        return employeeRepository.findAllDepartments();
    }

    // ---- Mapper Methods ----
    private Employee mapToEntity(EmployeeDTO dto) {
        Employee e = new Employee();
        e.setFirstName(dto.getFirstName());
        e.setLastName(dto.getLastName());
        e.setEmail(dto.getEmail());
        e.setDepartment(dto.getDepartment());
        e.setDesignation(dto.getDesignation());
        e.setSalary(dto.getSalary());
        e.setPhoneNumber(dto.getPhoneNumber());
        e.setJoiningDate(dto.getJoiningDate());
        if (dto.getStatus() != null) {
            e.setStatus(dto.getStatus());
        }
        return e;
    }

    private EmployeeDTO mapToDTO(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setFirstName(e.getFirstName());
        dto.setLastName(e.getLastName());
        dto.setEmail(e.getEmail());
        dto.setDepartment(e.getDepartment());
        dto.setDesignation(e.getDesignation());
        dto.setSalary(e.getSalary());
        dto.setPhoneNumber(e.getPhoneNumber());
        dto.setJoiningDate(e.getJoiningDate());
        dto.setStatus(e.getStatus());
        return dto;
    }
}
