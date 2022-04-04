package com.springbatch.demo.batch;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.springbatch.demo.model.Employee;

@Component
public class Processor implements ItemProcessor<Employee, Employee>{
	
	  private Set<Employee> seenUsers = new HashSet<Employee>();
	  
	    @Override
	    public Employee process(Employee employee) throws Exception {
	    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	         Date configDate = sdf.parse("1995-01-01");


	         int result = configDate.compareTo(employee.getDob());
	         
	    	  if(seenUsers.contains(employee)) {
	              return null;
	          }
	    	  if(employee!=null && employee.getRating() >=90) {
		    		employee.setHighPerfomer(true);
		      }
	    	  if(result > 0) {
	    		  employee.setSeniorEmployee(true);
	    	  }
	         seenUsers.add(employee);
	         return employee;
	    }
}
