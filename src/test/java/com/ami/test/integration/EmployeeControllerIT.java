package com.ami.test.integration;

import com.ami.test.model.Employee;
import com.ami.test.repository.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.xml.transform.Result;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIT extends AbstractionBaseTest{
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }

    @DisplayName("Integration Test for Create Employee Operation")
    @Test
    public void givenEmployee_whenCreateEmployee_thenReturnEmployee() throws Exception {
        Employee employee=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();


        ResultActions response=mockMvc.perform(post("/api/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));

    }

    @DisplayName("Integration test for Get Employees Operation")
    @Test
    public void givenEmployeeList_whenGetEmployees_thenReturnEmployeeList() throws Exception {
        Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();

        Employee anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();

        List<Employee> employeeList=List.of(ami,anurag);

        employeeRepository.saveAll(employeeList);

        ResultActions response= mockMvc.perform(get("/api/employees/"));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(employeeList.size())))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @DisplayName("Integration Test for Get Employee By Id Operation - Positive Scenario")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();

        employeeRepository.save(ami);

        ResultActions response= mockMvc.perform(get("/api/employees/{id}",ami.getId()));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(ami.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(ami.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(ami.getEmail())))
                .andExpect(MockMvcResultMatchers.status().isOk());



    }

    @DisplayName("Integration Test for Get Employee By Id - Negative Scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNothing() throws Exception {
        Employee employee= Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();

        employeeRepository.save(employee);

        ResultActions response= mockMvc.perform(get("/api/employees/{id}",2));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName("Integration Test for Update Employee - Positive Scenario")
    @Test
    public void givenEmployeeId_whenUpdateEmployee_thenReturnEmployee() throws Exception {
        Employee employee=Employee.builder()
                .firstName("Ami")
                .lastName("Anurag")
                .email("ami@gmail.com")
                .build();

        Employee updateEmployee=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();

        employeeRepository.save(employee);

        ResultActions response= mockMvc.perform(put("/api/employees/{id}",employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(updateEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(updateEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(updateEmployee.getEmail())))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @DisplayName("Integration Test for Update Employee - Negative Scenario")
    @Test
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturnEmployee() throws Exception {
        Employee employee=Employee.builder()
                .firstName("Ami")
                .lastName("Anurag")
                .email("ami@gmail.com")
                .build();

        Employee updateEmployee=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();

        employeeRepository.save(employee);

        ResultActions response= mockMvc.perform(put("/api/employees/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName("Integration Test for Delete Employee")
    @Test
    public void givenEmployeeId_whenDeleteEmployeeById_thenReturnNothing() throws Exception {
        Employee ami= Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        employeeRepository.save(ami);

        ResultActions response= mockMvc.perform(delete("/api/employees/{id}",ami.getId()));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
