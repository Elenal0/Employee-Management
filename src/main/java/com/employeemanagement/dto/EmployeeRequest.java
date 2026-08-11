package com.employeemanagement.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequest(
    @NotBlank @Size(min = 2, max = 50) String firstName,
    @NotBlank @Size(min = 2, max = 50) String lastName,
    @NotBlank @Email String email,
    String phoneNumber,
    @NotNull LocalDate hireDate,
    @NotBlank String jobTitle,
    @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal salary
) {}
