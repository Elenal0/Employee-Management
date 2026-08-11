package com.employeemanagement.controller;

import com.employeemanagement.dto.EmployeeRequest;
import com.employeemanagement.dto.EmployeeResponse;
import com.employeemanagement.model.Employee;
import com.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Register a new employee
    @PostMapping
    public ResponseEntity<EmployeeResponse> registerEmployee(@Valid @RequestBody EmployeeRequest request) {
        Employee employee = mapToEntity(request);
        Employee saved = employeeService.registerEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(saved));
    }

    // Get a single employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(mapToResponse(employeeService.getEmployeeById(id)));
    }

    // Get all employees
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> list = employeeService.getAllEmployees()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Get employees by department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponse>> getByDepartment(@PathVariable Long departmentId) {
        List<EmployeeResponse> list = employeeService.getEmployeesByDepartment(departmentId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Get employees by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeResponse>> getByStatus(@PathVariable String status) {
        List<EmployeeResponse> list = employeeService.getEmployeesByStatus(status)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Update employee profile
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id,
                                                           @Valid @RequestBody EmployeeRequest request) {
        Employee employee = mapToEntity(request);
        Employee updated = employeeService.updateEmployeeProfile(id, employee);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    // Allocate employee to a department
    @PutMapping("/{id}/department/{departmentId}")
    public ResponseEntity<EmployeeResponse> allocateDepartment(@PathVariable Long id,
                                                               @PathVariable Long departmentId) {
        Employee updated = employeeService.allocateDepartment(id, departmentId);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    // Terminate employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> terminateEmployee(@PathVariable Long id) {
        employeeService.terminateEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // --- Mappers ---
    private Employee mapToEntity(EmployeeRequest req) {
        Employee emp = new Employee();
        emp.setFirstName(req.firstName());
        emp.setLastName(req.lastName());
        emp.setEmail(req.email());
        emp.setPhoneNumber(req.phoneNumber());
        emp.setHireDate(req.hireDate());
        emp.setJobTitle(req.jobTitle());
        emp.setSalary(req.salary());
        return emp;
    }

    private EmployeeResponse mapToResponse(Employee emp) {
        return new EmployeeResponse(
                emp.getId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getEmail(),
                emp.getPhoneNumber(),
                emp.getHireDate(),
                emp.getJobTitle(),
                emp.getSalary(),
                emp.getStatus() != null ? emp.getStatus().name() : null,
                emp.getDepartment() != null ? emp.getDepartment().getName() : null
        );
    }
}
