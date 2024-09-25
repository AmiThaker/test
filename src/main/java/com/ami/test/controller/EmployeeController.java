package com.ami.test.controller;

import com.ami.test.model.Employee;
import com.ami.test.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees/")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService=employeeService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Employee createEmployee(@RequestBody  Employee employee){
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id)
                .map(employee->ResponseEntity.ok(employee))
                .orElseGet(()->ResponseEntity.notFound().build());
    }
    
    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable Long id,@RequestBody Employee employee){
        return employeeService.getEmployeeById(id)
                .map(e->{
                    e.setEmail(employee.getEmail());
                    e.setFirstName(employee.getFirstName());
                    e.setLastName(employee.getLastName());

                    Employee updatedEmployee=employeeService.updateEmployee(e);
                    return new ResponseEntity<>(updatedEmployee,HttpStatus.OK);
                })
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Long id){
            employeeService.deleteEmployee(id);
            return new ResponseEntity<String>("Emplpyee deleted Successfully!",HttpStatus.OK);
    }


}
