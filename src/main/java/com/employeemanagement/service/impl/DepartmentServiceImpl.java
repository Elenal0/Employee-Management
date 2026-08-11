package com.employeemanagement.service.impl;

import com.employeemanagement.exception.ResourceAlreadyExistsException;
import com.employeemanagement.exception.ResourceNotFoundException;
import com.employeemanagement.model.Department;
import com.employeemanagement.repository.DepartmentRepository;
import com.employeemanagement.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(Department department) {
        if (departmentRepository.findByCode(department.getCode()).isPresent()) {
            throw new ResourceAlreadyExistsException("Department with code " + department.getCode() + " already exists");
        }
        if (departmentRepository.findByName(department.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Department with name " + department.getName() + " already exists");
        }
        return departmentRepository.save(department);
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentByCode(String code) {
        return departmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with code " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
