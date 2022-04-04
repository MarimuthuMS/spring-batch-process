package com.springbatch.demo;



import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;

import com.springbatch.demo.batch.DBWriter;
import com.springbatch.demo.batch.Processor;
import com.springbatch.demo.model.Employee;
import com.springbatch.demo.repository.EmployeeRepository;
import com.springbatch.demo.springbatchconfig.SpringBatchConfig;
@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbatchApplicationTests {

	@Autowired
	@Mock  private JobLauncherTestUtils testUtils;

    @Autowired
    @Mock  private SpringBatchConfig config;
    
    @Autowired
    @Mock  private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    @Mock  private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    @Mock  private DBWriter dbWriter;
    
    @Autowired
    @Mock  private Processor processor;
    
    @Mock  private ItemProcessor<Employee, Employee> processor1;
    

    @Mock private ItemReader<Employee> itemReader1;
	
	@Mock 	private ItemWriter<Employee> itemWriter1;
    
	

    @Autowired
    private FlatFileItemReader<Employee> reader;
	
	@Mock private EmployeeRepository employeeRepository;


    
    @Test
    public void testEntireJob() throws Exception {
    	
 	final JobExecution result = (JobExecution) testUtils.getJobLauncher().run(config.job(jobBuilderFactory, stepBuilderFactory,  itemReader1,
        		processor1,
        		itemWriter1), testUtils.getUniqueJobParameters());
        Assert.assertNotNull(result);
        Assert.assertEquals(BatchStatus.COMPLETED, result.getStatus());
    }
    

    @Test
    public void testSpecificStep() {
        Assert.assertEquals(BatchStatus.COMPLETED, testUtils.launchStep("ETL-file-load").getStatus());
    }
    
    @Test
    public void testProcessorStep() throws Exception {
    	Date date = new Date(0);  
    	Employee employee1=new Employee(12345, "Raj",date,90.0,"4536643");
    	Assert.assertEquals(processor.process(employee1), employee1);
       }
    
    


    
    @Test
    public void testNegativeProcessorStep() throws Exception {
        Exception exception = assertThrows(NullPointerException.class, () -> {
        	
        	processor.process(null);
        });

        String expectedMessage = "null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    public void testWriter() throws Exception {
    	Date date = new Date(0);  
    	Employee employee1=new Employee(12345, "Raj",date,90.0,"4536643");
    	Employee employee2=new Employee(4344, "Kanaga",date,60.0,"432213");
    	Employee employee3=new Employee(232, "VIswa",date,50.0,"53223");
    	Employee employee4=new Employee(2332, "Saran",date,60.0,"677464");
    	Employee employee5=new Employee(2323, "Kanna",date,50.0,"545373");
    	List<Employee> namesList = Arrays.asList( employee1,employee3,employee2,employee4,employee5);
    	dbWriter.write(namesList);
       
    }
  
    
    @Test
    @DisplayName("Test save Employee")
    void testEmployeeSave() {
    	Date date = new Date(0);  
        // Setup our mock repository
     	Employee employee1=new Employee(12345, "Raj",date,90.0,"4536643");
        doReturn(employee1).when(employeeRepository).save(any());

        // Execute the service call
        Employee returnedEmployee = employeeRepository.save(employee1);

        // Assert the response
        Assertions.assertNotNull(returnedEmployee, "The saved Employee should not be null");
        Assertions.assertEquals(12345, returnedEmployee.getId(), "The ID should be primary key");
    }
  


    @Test
    public void givenMockedStep_whenReaderCalled_thenSuccess() throws Exception {
        // given
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("fileName", "input/*.csv");
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(ctx);

        // when
        List<Employee> items = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<Employee> result = new ArrayList<>();
            Employee item;
            reader.open(stepExecution.getExecutionContext());
            while ((item = reader.read()) != null) {
                result.add(item);
            }
            reader.close();
            return result;
        });

        // then
        Assert.assertEquals(30, items.size());
    }
    @Test
    public void givenMockedStep_whenReaderCalled_thenFail() throws Exception {
        // given
        ExecutionContext ctx = new ExecutionContext();
       // ctx.put("fileName", "input/employee1.csv");
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(ctx);
        Exception exception = assertThrows(ItemStreamException .class, () -> {
        	
	          List<Employee> items = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
	          List<Employee> result = new ArrayList<>();
	          Employee item;
	          reader.open(stepExecution.getExecutionContext());
	          while ((item = reader.read()) != null) {
	              result.add(item);
	          }
	          reader.close();
	          return result;
	           });
        });
        String expectedMessage = "Failed";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        // when
        

       
    }
}
