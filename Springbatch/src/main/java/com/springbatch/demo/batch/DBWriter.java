package com.springbatch.demo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.springbatch.demo.model.Employee;
import com.springbatch.demo.repository.EmployeeRepository;

import java.util.List;

	@Component
	public class DBWriter implements ItemWriter<Employee> {
		
		Logger logger = LoggerFactory.getLogger(DBWriter.class);

	    private EmployeeRepository employeeRepository;

		@Autowired
		public DBWriter(EmployeeRepository employeeRepository) {
			this.employeeRepository = employeeRepository;
		}

	    @Override
	    public void write(List<? extends Employee> employee) throws Exception{
	    	logger.info("Employee data before save");
	        employeeRepository.saveAll(employee);
	        logger.info("Employee data before save");
	      
	    }
	
}
