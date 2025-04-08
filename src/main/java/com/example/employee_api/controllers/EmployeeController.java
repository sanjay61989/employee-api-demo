package com.example.employee_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.example.employee_api.models.Employee;
import com.example.employee_api.repository.EmployeeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Employee API", description = "Operations related to employees")
@RestController
@RequestMapping("/employees")
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

    @GetMapping("/vp")
    public String fetchVipasna(int page) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Accept-Language", "en-US,en;q=0.9");
        headers.setCacheControl(CacheControl.noCache());

        String daterange = LocalDate.now() + " - 2026-04-07";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("current_state", "NewStudents");
        body.add("regions[]", "region_118");
        body.add("daterange", daterange);
        body.add("course_type_constraint[]", "3");
        body.add("course_type_constraint[]", "31");
        body.add("languages[]", "hi");
        body.add("page", String.valueOf(page));
        body.add("sort_column", "dates");
        body.add("sort_direction", "up");
        body.add("date_format", "YYYY-MM-DD");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        String url = "https://www.dhamma.org/en/courses/do_search"; // or the real endpoint

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response.getBody();
    }

}
