package com.springbatch.process.junit;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springbatch.process.config.SpringBatchConstant;
import com.springbatch.process.model.Employee;
import com.springbatch.process.service.ProcessorService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProcessorServiceTest {
	@Autowired
	@Mock
	private ProcessorService processor;
	
	@Value("${input.seniority.date}")
	private String seniorityDate;
	
	@Value("${input.perfomer.rating}")
	private Double highPerformRate;
	
	@Test
	@DisplayName("Test_Processor_Success")
	public void testProcessorStep() throws Exception {
		Date date = new Date(0);
		Employee employee1 = new Employee(12345, "Raj", date, 90.0, "4536643");
		Assert.assertEquals(processor.process(employee1), employee1);
	}

	@Test
	@DisplayName("Test_Processor_Failure")
	public void testNegativeProcessorStep() throws Exception {
		Exception exception = assertThrows(NullPointerException.class, () -> {
			processor.process(null);
		});

		String expectedMessage = "null";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	@DisplayName("Test_High_Performer_Success")
	public void checkHighPerformer() throws Exception {
		Date configDate = new Date(0);
		boolean isHighPerformer = true;
		Employee employee1 = new Employee(12345, "Raj", configDate, 91.0, "4536643");

		// Set High Performer based on Rating condition
		Stream.of(employee1).filter(emp -> emp.getRating() > highPerformRate)
				.peek(empObj -> empObj.setHighPerfomer(true)).collect(Collectors.toList());
		Assertions.assertEquals(employee1.isHighPerfomer(), isHighPerformer, "Employee High Performer");

	}
	
	@Test
	@DisplayName("Test_High_Performer_Failure")
	public void checkHighPerformerNegative() throws Exception {
		Date configDate = new Date(0);
		boolean isHighPerformer = false;
		Employee employee1 = new Employee(12345, "Raj", configDate, 80.0, "4536643");

		// Set High Performer based on Rating condition
		Stream.of(employee1).filter(emp -> emp.getRating() < highPerformRate)
				.peek(empObj -> empObj.setHighPerfomer(false)).collect(Collectors.toList());
		Assertions.assertEquals(employee1.isHighPerfomer(), isHighPerformer, "Employee High Performer");

	}

	@Test
	@DisplayName("Test_Seniority_Success")
	public void checkSeniority() throws Exception {
		@SuppressWarnings("deprecation")
		java.sql.Date configDate = new java.sql.Date(1994, 01, 01);
		   @SuppressWarnings("deprecation")
		     java.sql.Date configDate1 = new java.sql.Date(1995, 01, 01);

		boolean seniority = true;
		Employee employee1 = new Employee(12345, "Raj", configDate, 90.0, "4536643");
		Stream.of(employee1).filter(emp -> configDate1.compareTo(emp.getDob()) > 0)
				.peek(empObj -> empObj.setSeniorEmployee(true)).collect(Collectors.toList());
		Assertions.assertEquals(employee1.isSeniorEmployee(), seniority, "Employee Seniority");

	}

	@Test
	@DisplayName("Test_Seniority_Failure")
	public void checkSeniorityNegative() throws Exception {
		@SuppressWarnings("deprecation")
		java.sql.Date configDate = new java.sql.Date(1996, 01, 01);
		   @SuppressWarnings("deprecation")
		     java.sql.Date configDate1 = new java.sql.Date(1995, 01, 01);

		boolean seniority = false;
		Employee employee1 = new Employee(12345, "Raj", configDate, 90.0, "4536643");
		Stream.of(employee1).filter(emp -> configDate1.compareTo(emp.getDob()) < 0)
				.peek(empObj -> empObj.setSeniorEmployee(false)).collect(Collectors.toList());
		Assertions.assertEquals(employee1.isSeniorEmployee(), seniority, "Employee Seniority");

	}
}
