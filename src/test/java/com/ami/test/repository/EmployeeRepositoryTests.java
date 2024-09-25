package com.ami.test.repository;

import com.ami.test.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee ami;
    private Employee anurag;

    @BeforeEach
    public void setUp(){
        ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();
    }

    @DisplayName("JUnit test for Save Employee Operation")
    @Test
    public void givenEmployee_whenSave_thenReturnSavedEmployee(){
        /*Employee employee=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();*/
        Employee savedEmployee=employeeRepository.save(ami);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("JUnit Tests for get Employee Operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList(){
        /*Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        Employee anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();*/

        employeeRepository.save(ami);
        employeeRepository.save(anurag);

        List<Employee> employeeList=employeeRepository.findAll();

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit Test for Get Employee By ID Operation")
    @Test
    public void givenEmployeeList_whenFindById_thenEmployee(){
        /*Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        Employee anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();*/

        employeeRepository.save(ami);
        employeeRepository.save(anurag);

        Employee employee=employeeRepository.findById(ami.getId()).get();

        assertThat(employee).isNotNull();

    }

    @DisplayName("JUnit test for getting Employee by Email")
    @Test
    public void givenEmail_whenFindByEmail_thenEmployee(){
        /*Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        Employee anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();*/

        employeeRepository.save(ami);
        employeeRepository.save(anurag);

        Employee employee=employeeRepository.findByEmail(ami.getEmail()).get();

        assertThat(employee).isEqualTo(ami);
    }

    @DisplayName("JUnit Test for Update Employee")
    @Test
    public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        /*Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        Employee anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();*/

        employeeRepository.save(ami);
        employeeRepository.save(anurag);

        Employee employee=employeeRepository.findById(ami.getId()).get();

        employee.setEmail("ami123@gmail.com");
        employee.setFirstName("Amirag");
        
        Employee updatedEmployee=employeeRepository.save(employee);

        assertThat(updatedEmployee.getEmail()).isEqualTo("ami123@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Amirag");

    }

    @DisplayName("JUnit Test for Delete Employee Operation")
    @Test
    public void givenEmployeeList_whenDelete_thenRemoveEmployee(){
        /*Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        Employee anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();*/

        employeeRepository.save(ami);
        employeeRepository.save(anurag);

        employeeRepository.delete(ami);
        Optional<Employee> employee=employeeRepository.findById(ami.getId());

        assertThat(employee).isEmpty();
    }


    @DisplayName("JUnit Test for Get Employee By First Name And Last Name Operation")
    @Test
    public void givenEmployeeList_whenGetEmployeeByFirstNameAndLastName_thenReturnEmployee(){
        /*Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        Employee anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();*/

        employeeRepository.save(ami);
        employeeRepository.save(anurag);

        Optional<Employee> employee=employeeRepository.findByFirstNameAndLastName(ami.getFirstName(),ami.getLastName());

        assertThat(employee).get().isNotNull();
        assertThat(employee).get().isEqualTo(ami);
    }

    @DisplayName("JUnit Test for Get Employee By Named Params")
    @Test
    public void givenEmployeeList_whenGetEmployeeByNamedParams_thenReturnEmployee(){
        /*Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        Employee anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();*/

        employeeRepository.save(ami);
        employeeRepository.save(anurag);

        Optional<Employee> employee=employeeRepository.findByNamedParams(ami.getFirstName(),ami.getLastName());

        assertThat(employee).get().isNotNull();
        assertThat(employee).get().isEqualTo(ami);
    }

    @DisplayName("JUnit Test for Get Employee By Native SQL Query Operation")
    @Test
    public void givenEmployeeList_whenGetEmployeeByNativeSQL_thenReturnEmployee(){
        /*Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        Employee anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();*/

        employeeRepository.save(ami);
        employeeRepository.save(anurag);

        Optional<Employee> employee=employeeRepository.findByNativeSQL(ami.getFirstName(),ami.getLastName());

        assertThat(employee).get().isNotNull();
        assertThat(employee).get().isEqualTo(ami);
    }

    @DisplayName("JUnit Test for Get Employee By Native SQL Named Params")
    @Test
    public void givenEmployeeList_whenGetEmployeeByNativeSQLNamedParams_thenReturnEmployee(){
        /*Employee ami=Employee.builder()
                .firstName("Ami")
                .lastName("Thaker")
                .email("ami@gmail.com")
                .build();
        Employee anurag=Employee.builder()
                .firstName("Anurag")
                .lastName("Dubey")
                .email("anu@gmail.com")
                .build();*/

        employeeRepository.save(ami);
        employeeRepository.save(anurag);

        Optional<Employee> employee=employeeRepository.findByNativeSQLNamedParams(ami.getFirstName(),ami.getLastName());

        assertThat(employee).get().isNotNull();
        assertThat(employee).get().isEqualTo(ami);
    }
}
