package com.springbatch.demo;



import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.springbatch.demo.springbatchconfig.SpringBatchConfig;



@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SpringBatchConfig.class, BatchTestConfiguration.class})
public class EmployeeReportJobConfigTest {
	@Autowired
    private JobLauncherTestUtils testUtils;

    @Autowired
    private SpringBatchConfig config;

    @Test
    public void testEntireJob() throws Exception {
        final JobExecution result = (JobExecution) testUtils.getJobLauncher().run(config.job(null, null, null, null, null), testUtils.getUniqueJobParameters());
        Assert.assertNotNull(result);
        Assert.assertEquals(BatchStatus.COMPLETED, result.getStatus());
    }

    @Test
    public void testSpecificStep() {
        Assert.assertEquals(BatchStatus.COMPLETED, testUtils.launchStep("taskletStep").getStatus());
    }
}
