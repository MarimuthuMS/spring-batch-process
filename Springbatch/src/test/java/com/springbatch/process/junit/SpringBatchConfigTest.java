package com.springbatch.process.junit;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import com.springbatch.process.config.SpringBatchConfig;
import com.springbatch.process.model.Employee;
import com.springbatch.process.repository.EmployeeRepository;
import com.springbatch.process.service.DBWriterService;
import com.springbatch.process.service.ProcessorService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SpringBatchConfigTest {
	@Autowired
	@Mock  private JobLauncherTestUtils testUtils;

    @Autowired
    @Mock  private SpringBatchConfig config;
    
    @Autowired
    @Mock  private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    @Mock  private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    @Mock  private DBWriterService dbWriter;
    
    @Autowired
    @Mock  private ProcessorService processor;
    
    @Mock  private ItemProcessor<Employee, Employee> processor1;
    

    @Mock private ItemReader<Employee> itemReader1;
	
	@Mock 	private ItemWriter<Employee> itemWriter1;
    
	

    @Autowired
    private FlatFileItemReader<Employee> reader;
	
	@Mock private EmployeeRepository employeeRepository;


    
    @Test 
    @DisplayName("Test_Job_Execution_Success")
    public void test_EntireJob() throws Exception {
    	
 	final JobExecution result = (JobExecution) testUtils.getJobLauncher().run(config.job(jobBuilderFactory, stepBuilderFactory,  itemReader1,
        		processor1,
        		itemWriter1), testUtils.getUniqueJobParameters());
        Assert.assertNotNull(result);
        Assert.assertEquals("Test_Job_Execution_Success",BatchStatus.COMPLETED, result.getStatus());
    }
    
    @Test 
    @DisplayName("Test_Job_Execution_Failure")
    public void test_Negative_Senario_Job() throws Exception {
    	JobExecution result=null;
    	  try
          {
    		  result = (JobExecution) testUtils.getJobLauncher().run(config.job(null, null,  null,
    	        		null,
    	        		null), null);
              Assertions.fail();
          }
          catch(IllegalArgumentException ex) 
          {
        	    String expectedMessage = "null";
                String actualMessage = ex.getMessage();
                assertTrue("Test_Job_Execution_Failure",actualMessage.contains(expectedMessage));
                
          }
 	
 	
    }
   
    
    @Test
    @DisplayName("Test_Step_Success")
    public void testSpecificStep() throws Exception{
        Assert.assertEquals("Test_Step_Success",BatchStatus.COMPLETED, testUtils.launchStep("ETL-file-load").getStatus());
    }
    
    @Test
    @DisplayName("Test_Step_Failure")
    public void testNegativeSpecificStep() throws Exception{
    	try {
    	      Assert.assertEquals("Test_Step_Failure",BatchStatus.FAILED, testUtils.launchStep(null).getStatus());
    	      
    	}
    	catch(IllegalStateException ex) {
    		 String expectedMessage = "null";
             String actualMessage = ex.getMessage();
             assertTrue("Test_Step_Failure",actualMessage.contains(expectedMessage));
    	}
     }
    

    @Test
    @DisplayName("Test_File_Reader_Success")
    public void givenMockedStep_whenReaderCalled_thenSuccess() throws Exception {
        // given
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("fileName", "input/employee1.csv");
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
        Assert.assertEquals("Test_File_Reader_Success",30, items.size());
    }
    
    @Test
    @DisplayName("Test_File_Reader_Failure")
    public void givenMockedStep_whenReaderCalled_thenFail() throws Exception {
        // given
        ExecutionContext ctx = new ExecutionContext();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(ctx);
        Exception exception = assertThrows(ItemStreamException.class, () -> {
        	
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
        assertTrue("Test_File_Reader_Failure",actualMessage.contains(expectedMessage));
       
    }
}
