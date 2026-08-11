package com.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.employeemanagement.dto.DepartmentRequest;
import com.employeemanagement.dto.EmployeeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateDepartmentSuccessfully() throws Exception {
        DepartmentRequest request = new DepartmentRequest("Engineering", "ENG");
        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Engineering"))
                .andExpect(jsonPath("$.code").value("ENG"));
    }

    @Test
    void shouldRegisterEmployeeSuccessfully() throws Exception {
        EmployeeRequest request = new EmployeeRequest(
                "Alice", "Smith", "alice@example.com",
                "9876543210", LocalDate.of(2024, 1, 15),
                "Software Engineer", new BigDecimal("75000.00")
        );
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        EmployeeRequest request = new EmployeeRequest(
                "Alice", "Smith", "duplicate@example.com",
                null, LocalDate.of(2024, 1, 15), "Engineer", new BigDecimal("60000.00")
        );
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Second registration with same email
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFoundForMissingEmployee() throws Exception {
        mockMvc.perform(get("/api/employees/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAllocateDepartmentToEmployee() throws Exception {
        // Create department first
        DepartmentRequest deptReq = new DepartmentRequest("HR", "HR01");
        String deptResult = mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deptReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long deptId = objectMapper.readTree(deptResult).get("id").asLong();

        // Register employee
        EmployeeRequest empReq = new EmployeeRequest(
                "Bob", "Jones", "bob@example.com",
                null, LocalDate.of(2024, 3, 1), "HR Manager", new BigDecimal("55000.00")
        );
        String empResult = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long empId = objectMapper.readTree(empResult).get("id").asLong();

        // Allocate
        mockMvc.perform(put("/api/employees/" + empId + "/department/" + deptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName").value("HR"));
    }

    @Test
    void shouldTerminateEmployee() throws Exception {
        EmployeeRequest empReq = new EmployeeRequest(
                "Carol", "White", "carol@example.com",
                null, LocalDate.now(), "Analyst", new BigDecimal("50000.00")
        );
        String result = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long empId = objectMapper.readTree(result).get("id").asLong();

        // Terminate
        mockMvc.perform(delete("/api/employees/" + empId))
                .andExpect(status().isNoContent());
    }
}
