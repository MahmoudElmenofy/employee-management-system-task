package com.it.ranks.technology.employee.management.service;

import com.it.ranks.technology.employee.management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Page<Employee> getAllEmployees(Pageable pageable);
    Optional<Employee> getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee employeeDetails);
    void deleteEmployee(Long id);
    List<Employee> getEmployeesByDepartment(String department);
}
