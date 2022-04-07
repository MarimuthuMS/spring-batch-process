package com.springbatch.process.junit;

import static org.junit.Assert.assertTrue;


import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.springbatch.process.model.Employee;
import com.springbatch.process.repository.EmployeeRepository;
import com.springbatch.process.service.DBWriterService;
import com.springbatch.process.service.ProcessorService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DBWriterServiceTest {
	@Autowired
	@Mock
	private DBWriterService dbWriter;

	@Autowired
	@Mock
	private ProcessorService processor;

	@Mock
	private ItemReader<Employee> itemReader1;

	@Mock
	private ItemWriter<Employee> itemWriter1;

	@Mock
	private EmployeeRepository employeeRepository;

	@Test
	@DisplayName("Test_Writer_Success")
	public void testWriter() throws Exception {
		Date date = new Date(0);
		Employee employee1 = new Employee(12345, "Raj", date, 90.0, "4536643");
		Employee employee2 = new Employee(4344, "Kanaga", date, 60.0, "432213");
		Employee employee3 = new Employee(232, "VIswa", date, 50.0, "53223");
		Employee employee4 = new Employee(2332, "Saran", date, 60.0, "677464");
		Employee employee5 = new Employee(2323, "Kanna", date, 50.0, "545373");
		List<Employee> namesList = Arrays.asList(employee1, employee3, employee2, employee4, employee5);
		dbWriter.write(namesList);

	}

	@Test
	@DisplayName("Test_Writer_Failure")
	public void testNegativeScenarioWriter() throws Exception {
		try {
			dbWriter.write(null);
			Assertions.fail();
		} catch (Exception ex) {
			String expectedMessage = "null";
			String actualMessage = ex.getMessage();
			assertTrue("Test_Writer_Failure", actualMessage.contains(expectedMessage));
		}

	}
}
