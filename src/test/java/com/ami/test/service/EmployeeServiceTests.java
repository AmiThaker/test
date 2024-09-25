package com.ami.test.service;

import com.ami.test.exception.ResourceNotFoundException;
import com.ami.test.model.Employee;
import com.ami.test.repository.EmployeeRepository;
import jakarta.inject.Inject;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        employee=Employee.builder()
                .id(1L)
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
    }

    @DisplayName("JUnit Test for Save Employee Operation")
    @Test
    public void givenEmployee_whenSaveEmployee_thenReturnEmployee(){
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        Employee savedEmployee=employeeService.saveEmployee(employee);

        System.out.println(savedEmployee.toString());

        assertThat(savedEmployee).isNotNull();

    }

    @DisplayName("JUnit Test for save Employee Method which throws Exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        //given(employeeRepository.save(employee)).willReturn(employee);

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,()->{
           employeeService.saveEmployee(employee);
        });

        verify(employeeRepository,never()).save(any(Employee.class));
    }

    @DisplayName("JUnit Test for Get All Employees Method")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList(){

        Employee employee1=Employee.builder()
                .id(2L)
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));

        List<Employee> employeeList=employeeService.getAllEmployees();

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit Test For Get All Employees Method - Passing Empty List")
    @Test
    public void givenEmptyList_whenGetAllEmployees_thenReturnEmptyList(){
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        List<Employee> employeeList=employeeService.getAllEmployees();

        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }


    @DisplayName("JUnit Test Operation For Get Employee By Id")
    @Test
    public void givenEmployeeId_whenFindEmployeeById_thenReturnEmployee(){
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        Employee savedEmployee=employeeService.getEmployeeById(employee.getId()).get();

        assertThat(savedEmployee).isNotNull();

    }

    @DisplayName("JUnit Test for Update Employee Operation")
    @Test
    public void givenEmployee_whenUpdateEmployee_returnEmployee(){
        given(employeeRepository.save(employee)).willReturn(employee);

        employee.setEmail("ami123@gmail.com");

        Employee updatedEmployee=employeeService.saveEmployee(employee);

        assertThat(updatedEmployee.getEmail()).isEqualTo("ami123@gmail.com");
    }

    @DisplayName("JUnit Test for Delete Employee by ID Operation")
    @Test
    public void givenEmployeeId_whenDeleteEmployeeById_thenNothing(){
        willDoNothing().given(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository,times(1)).deleteById(1L);
    }
}
