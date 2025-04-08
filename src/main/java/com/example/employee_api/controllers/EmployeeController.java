package com.example.employee_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employee_api.models.Employee;
import com.example.employee_api.repository.EmployeeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Employee API", description = "Operations related to employees")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository repo;

    @Operation(summary = "Get all employees")
    @GetMapping("/all")
    public List<Employee> all() {
        return repo.findAll();
    }

    @Operation(summary = "Create a new employee")
    @PostMapping("/create")
    public Employee create(@RequestBody Employee emp) {
        return repo.save(emp);
    }

    @Operation(summary = "Get a single employee by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee data) {
        return repo.findById(id).map(emp -> {
            emp.setName(data.getName());
            emp.setDepartment(data.getDepartment());
            emp.setEmail(data.getEmail());
            return ResponseEntity.ok(repo.save(emp));
        }).orElse(ResponseEntity.notFound().build());
    }

}
