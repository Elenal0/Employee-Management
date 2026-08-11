package com.employeemanagement.service;

import com.employeemanagement.model.Employee;
import java.util.List;

public interface EmployeeService {
    Employee registerEmployee(Employee employee);
    Employee getEmployeeById(Long id);
    List<Employee> getAllEmployees();
    List<Employee> getEmployeesByDepartment(Long departmentId);
    List<Employee> getEmployeesByStatus(String status);
    Employee updateEmployeeProfile(Long id, Employee employeeDetails);
    Employee allocateDepartment(Long id, Long departmentId);
    void terminateEmployee(Long id);
}
