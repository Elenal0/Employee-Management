package com.employeemanagement.service;

import com.employeemanagement.model.Department;
import java.util.List;

public interface DepartmentService {
    Department createDepartment(Department department);
    Department getDepartmentById(Long id);
    Department getDepartmentByCode(String code);
    List<Department> getAllDepartments();
}
