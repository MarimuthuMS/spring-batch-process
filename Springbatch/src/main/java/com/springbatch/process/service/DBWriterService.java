package com.springbatch.process.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbatch.process.model.Employee;
import com.springbatch.process.repository.EmployeeRepository;

import java.util.List;

@Service
public class DBWriterService implements ItemWriter<Employee> {

	Logger logger = LoggerFactory.getLogger(DBWriterService.class);

	private EmployeeRepository employeeRepository;

	// Constructor injection
	@Autowired
	public DBWriterService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	// All employee data persistence into DB
	@Override
	public void write(List<? extends Employee> employee) throws Exception {
		try {
			logger.info("Employee data before save");
			employeeRepository.saveAll(employee);
			logger.info("Employee data after save");
		} catch (Exception ex) {
			logger.error("Write Cause : " + ex.getCause());
		}

	}

}
