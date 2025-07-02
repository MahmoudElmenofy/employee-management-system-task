package com.it.ranks.technology.employee.management.controller;

import com.it.ranks.technology.employee.management.entity.Employee;
import com.it.ranks.technology.employee.management.exception.ResourceNotFoundException;
import com.it.ranks.technology.employee.management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity <Employee> createEmployee(@Valid @RequestBody Employee employee) {
        Employee created = employeeService.createEmployee(employee);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity <Page<Employee>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> result = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity <Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee updatedDetails) {

        Employee updated = employeeService.updateEmployee(id, updatedDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{departmentName}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable String departmentName) {
        List<Employee> result = employeeService.getEmployeesByDepartment(departmentName);
        return ResponseEntity.ok(result);
    }
}
