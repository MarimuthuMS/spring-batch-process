package com.springbatch.process.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springbatch.process.config.SpringBatchConstant;
import com.springbatch.process.model.Employee;

@Service
public class ProcessorService implements ItemProcessor<Employee, Employee> {

	@Value("${input.seniority.date}")
	private String seniorityDate;

	@Value("${input.perfomer.rating}")
	private Double highPerformRate;
	
	Logger logger = LoggerFactory.getLogger(ProcessorService.class);
	
	private Set<Employee> employeeList = new HashSet<Employee>();

	@Override
	public Employee process(Employee employee) throws Exception {
		try {
		SimpleDateFormat sdf = new SimpleDateFormat(SpringBatchConstant.SPRING_SIMPLE_DATE_FORMAT);
		Date configDate = sdf.parse(seniorityDate);
		if (employeeList.contains(employee))
			return null;
		// Set Seniority based on DOB condition
		Stream.of(employee).filter(emp -> configDate.compareTo(emp.getDob()) > 0)
				.peek(empObj -> empObj.setSeniorEmployee(true)).collect(Collectors.toList());
		
		// Set High Performer based on Rating condition
		Stream.of(employee).filter(emp -> emp.getRating() > highPerformRate)
				.peek(empObj -> empObj.setHighPerfomer(true)).collect(Collectors.toList());
		
		employeeList.add(employee);
		return employee;
		}catch(Exception ex) {
			logger.error("Process Cause : " + ex.getCause());
		}
		return null;
	}
}
