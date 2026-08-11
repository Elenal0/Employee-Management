package com.employeemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentRequest(
    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100)
    String name,

    @NotBlank(message = "Department code is required")
    @Size(min = 2, max = 10)
    String code
) {}
