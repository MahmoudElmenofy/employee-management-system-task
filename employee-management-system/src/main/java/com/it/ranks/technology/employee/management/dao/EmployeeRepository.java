package com.it.ranks.technology.employee.management.dao;

import com.it.ranks.technology.employee.management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartment(String department);
    boolean existsByEmail(String email);
}
