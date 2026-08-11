package com.employeemanagement.service.impl;

import com.employeemanagement.exception.ResourceAlreadyExistsException;
import com.employeemanagement.exception.ResourceNotFoundException;
import com.employeemanagement.model.Department;
import com.employeemanagement.model.Employee;
import com.employeemanagement.model.EmployeeStatus;
import com.employeemanagement.repository.DepartmentRepository;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Employee registerEmployee(Employee employee) {
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Employee with email " + employee.getEmail() + " already exists");
        }
        employee.setStatus(EmployeeStatus.ACTIVE);
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByStatus(String status) {
        EmployeeStatus enumStatus;
        try {
            enumStatus = EmployeeStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid employee status: " + status);
        }
        return employeeRepository.findByStatus(enumStatus);
    }

    @Override
    public Employee updateEmployeeProfile(Long id, Employee employeeDetails) {
        Employee existing = getEmployeeById(id);
        if (employeeDetails.getFirstName() != null) existing.setFirstName(employeeDetails.getFirstName());
        if (employeeDetails.getLastName() != null) existing.setLastName(employeeDetails.getLastName());
        if (employeeDetails.getEmail() != null && !employeeDetails.getEmail().equals(existing.getEmail())) {
            if (employeeRepository.findByEmail(employeeDetails.getEmail()).isPresent()) {
                throw new ResourceAlreadyExistsException("Employee with email " + employeeDetails.getEmail() + " already exists");
            }
            existing.setEmail(employeeDetails.getEmail());
        }
        if (employeeDetails.getPhoneNumber() != null) existing.setPhoneNumber(employeeDetails.getPhoneNumber());
        if (employeeDetails.getHireDate() != null) existing.setHireDate(employeeDetails.getHireDate());
        if (employeeDetails.getJobTitle() != null) existing.setJobTitle(employeeDetails.getJobTitle());
        if (employeeDetails.getSalary() != null) existing.setSalary(employeeDetails.getSalary());
        if (employeeDetails.getStatus() != null) existing.setStatus(employeeDetails.getStatus());
        return employeeRepository.save(existing);
    }

    @Override
    public Employee allocateDepartment(Long id, Long departmentId) {
        Employee employee = getEmployeeById(id);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + departmentId));
        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }

    @Override
    public void terminateEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employee.setStatus(EmployeeStatus.TERMINATED);
        employeeRepository.save(employee);
    }
}
