package com.employeemanagement.controller;

import com.employeemanagement.dto.DepartmentRequest;
import com.employeemanagement.dto.DepartmentResponse;
import com.employeemanagement.model.Department;
import com.employeemanagement.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // Create a new department
    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        Department dept = new Department(request.name(), request.code());
        Department saved = departmentService.createDepartment(dept);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(saved));
    }

    // Get a single department by ID
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(mapToResponse(departmentService.getDepartmentById(id)));
    }

    // Get all departments
    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        List<DepartmentResponse> list = departmentService.getAllDepartments()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Get department by code
    @GetMapping("/code/{code}")
    public ResponseEntity<DepartmentResponse> getDepartmentByCode(@PathVariable String code) {
        return ResponseEntity.ok(mapToResponse(departmentService.getDepartmentByCode(code)));
    }

    // --- Mapper ---
    private DepartmentResponse mapToResponse(Department dept) {
        return new DepartmentResponse(
                dept.getId(),
                dept.getName(),
                dept.getCode(),
                dept.getEmployees() != null ? dept.getEmployees().size() : 0
        );
    }
}
