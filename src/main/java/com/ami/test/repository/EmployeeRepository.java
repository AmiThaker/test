package com.ami.test.repository;

import com.ami.test.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Optional<Employee> findByEmail(String email);

    @Query("Select e from Employee e where e.firstName=?1 and e.lastName=?2")
    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("Select e from Employee e where e.firstName=:firstName and e.lastName=:lastName")
    Optional<Employee> findByNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(value="Select * from employees e where e.first_name=?1 and e.last_name=?2",nativeQuery = true)
    Optional<Employee> findByNativeSQL(String firstName,String lastName);

    @Query(value="select * from employees e where e.first_name=:firstName and e.last_name=:lastName",nativeQuery = true)
    Optional<Employee> findByNativeSQLNamedParams(String firstName, String lastName);
}
