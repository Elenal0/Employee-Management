package com.employeemanagement.dto;

public record DepartmentResponse(
    Long id,
    String name,
    String code,
    int employeeCount
) {}
