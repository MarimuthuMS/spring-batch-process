package com.springbatch.process.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springbatch.process.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

}
