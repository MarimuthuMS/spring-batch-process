package com.springbatch.demo.batch;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springbatch.demo.model.Employee;
import com.springbatch.demo.repository.EmployeeRepository;

import java.util.List;

	@Component
	public class DBWriter implements ItemWriter<Employee> {

	    private EmployeeRepository employeeRepository;

		@Autowired
		public DBWriter(EmployeeRepository employeeRepository) {
			this.employeeRepository = employeeRepository;
		}

	    @Override
	    public void write(List<? extends Employee> employee) throws Exception{
	        System.out.println("Data Saved for Employee: " + employee);
	        employeeRepository.saveAll(employee);
	      
	    }
	
}
