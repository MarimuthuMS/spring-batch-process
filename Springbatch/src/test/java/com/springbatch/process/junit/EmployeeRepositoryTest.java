package com.springbatch.process.junit;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.springbatch.process.model.Employee;
import com.springbatch.process.repository.EmployeeRepository;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeeRepositoryTest {

	@MockBean
	private EmployeeRepository employeeRepository;

	@Test
	@DisplayName("Test_Find_All_Employee")
	public void testFindAllEmployees() {
		Date date = new Date(0);
		Employee employee1 = new Employee(12345, "Raj", date, 90.0, "4536643");
		Employee employee2 = new Employee(4344, "Kanaga", date, 60.0, "432213");
		Employee employee3 = new Employee(232, "VIswa", date, 50.0, "53223");
		Employee employee4 = new Employee(2332, "Saran", date, 60.0, "677464");
		Employee employee5 = new Employee(2323, "Kanna", date, 50.0, "545373");
		List<Employee> list = Arrays.asList(employee1, employee3, employee2, employee4, employee5);
		when(employeeRepository.findAll()).thenReturn(list);
		List<Employee> empList = employeeRepository.findAll();
		assertEquals(5, empList.size());
		verify(employeeRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Test_Employee_Create")
	public void testCreateOrSaveEmployee() {
		Date date = new Date(0);
		Employee employee1 = new Employee(12345, "Raj", date, 90.0, "4536643");
		employeeRepository.save(employee1);
		verify(employeeRepository, times(1)).save(employee1);
	}

	@Test
	@DisplayName("Test_Employee_Create_Exception")
	public void testCreateOrSaveEmployeeException() throws Exception {

		try {
			employeeRepository.save(null);
		} catch (Exception ex) {
			String expectedMessage = "null";
			String actualMessage = ex.getMessage();
			assertTrue("Test_Employee_Create_Exception", actualMessage.contains(expectedMessage));
		}
	}

	@Test
	@DisplayName("Test_Employee_Delete")
	public void testCreateReadDelete() {
		Date date = new Date(0);
		Employee employee1 = new Employee(12345, "Raj", date, 90.0, "4536643");
		employeeRepository.save(employee1);
		employeeRepository.deleteAll();
		assertTrue("Test_Employee_Delete", employeeRepository.findAll().isEmpty());
	}
}
