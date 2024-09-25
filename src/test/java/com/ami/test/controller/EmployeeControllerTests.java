package com.ami.test.controller;

import com.ami.test.model.Employee;
import com.ami.test.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("JUnit Test for Create Employee Operation")
    @Test
    public void givenEmployeeObject_whenCreatEmployee_thenReturnEmployee() throws Exception{
        Employee employee=Employee.builder()
                .id(1L)
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();

        BDDMockito.given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response=mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("JUnit Test for Get All Employees Operation")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnAllEmployees() throws Exception {
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build());

        employeeList.add(Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build());

        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employeeList);

        ResultActions response=mockMvc.perform(get("/api/employees"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(employeeList.size())));
    }

    @DisplayName("JUnit Test for Get Employee By Id Operation - Positive Scenario")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        Employee employee= Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(1L)).willReturn(Optional.of(employee));

        ResultActions response= mockMvc.perform(get("/api/employees/{id}",1L));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("JUnit Test for Get Employee By Id - Negative Scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnOptionalEmpty() throws Exception {
        Employee employee=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(1L)).willReturn(Optional.empty());

        ResultActions response= mockMvc.perform(get("/api/employees/{id}",1L));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName("JUnit Test for Update Employee")
    @Test
    public void givenEmployeeIdAndObject_whenUpdateEmployee_thenReturnEmployeeObject() throws Exception {
        Employee employee= Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();

        Employee updateEmployee=Employee.builder()
                        .firstName("Anurag")
                                .lastName("Dubey")
                                        .email("anu@gmail.com")
                                                .build();

        BDDMockito.given(employeeService.getEmployeeById(1L)).willReturn(Optional.of(employee));
        BDDMockito.given(employeeService.updateEmployee(updateEmployee))
                .willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response= mockMvc.perform(put("/api/employees/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(updateEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(updateEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(updateEmployee.getEmail())));

    }

    @DisplayName("JUnit Test for Update Employee By Id - Negative Scenario")
    @Test
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturnEmployee() throws Exception {

        Employee employee=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();

        Employee updateEmployee=Employee.builder()
                        .firstName("Anurag")
                                .lastName("Dubey")
                                        .email("anu@gmail.com")
                                                .build();

        BDDMockito.given(employeeService.getEmployeeById(2L))
                .willReturn(Optional.empty());
        BDDMockito.given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response= mockMvc.perform(put("/api/employees/{id}",2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @DisplayName("JUnit Test for delete Employee by Id Operation")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() throws Exception {
        Employee employee=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(1L)).willReturn(Optional.of(employee));
        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(1L);

        ResultActions response=mockMvc.perform(delete("/api/employees/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
