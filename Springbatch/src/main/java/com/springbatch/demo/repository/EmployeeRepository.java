package com.springbatch.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springbatch.demo.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

}
