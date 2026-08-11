package com.employeemanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    LocalDate hireDate,
    String jobTitle,
    BigDecimal salary,
    String status,
    String departmentName
) {}
