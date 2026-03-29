package com.bipul.ems.controller;

import com.bipul.ems.dto.ApiResponse;
import com.bipul.ems.dto.EmployeeDTO;
import com.bipul.ems.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // CREATE - POST /api/employees
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO created = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", created));
    }

    // READ ALL - GET /api/employees
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(
                ApiResponse.success("Employees fetched successfully", employees));
    }

    // READ ONE - GET /api/employees/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success("Employee fetched successfully", employee));
    }

    // UPDATE - PUT /api/employees/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updated = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updated));
    }

    // DELETE - DELETE /api/employees/{id}  [ADMIN only]
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", null));
    }

    // SEARCH - GET /api/employees/search?keyword=
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> searchEmployees(
            @RequestParam String keyword) {
        List<EmployeeDTO> results = employeeService.searchEmployees(keyword);
        return ResponseEntity.ok(ApiResponse.success("Search results", results));
    }

    // BY DEPARTMENT - GET /api/employees/department/{dept}
    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getByDepartment(
            @PathVariable String department) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(department);
        return ResponseEntity.ok(ApiResponse.success("Employees in " + department, employees));
    }

    // ALL DEPARTMENTS - GET /api/employees/departments
    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<String>>> getAllDepartments() {
        List<String> departments = employeeService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("All departments", departments));
    }
}
