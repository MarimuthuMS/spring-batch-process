package com.springbatch.process.config;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;



@Configuration
@EnableScheduling
public class SpringBatchScheduler {
	
	Logger logger = LoggerFactory.getLogger(SpringBatchScheduler.class);
	
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

	SimpleDateFormat format = new SimpleDateFormat(SpringBatchConstant.SPRING_DATE_FORMAT);
    // cron job runs each and every one minutes
	@Scheduled(cron = "0 */1 * * * ?")
	public void scheduleByFixedRate() throws Exception {
		try {
		 logger.info("Batch job starting");
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("time", format.format(Calendar.getInstance().getTime())).toJobParameters();
		jobLauncher.run(job, jobParameters);
		 logger.info("Batch job executed successfully\n");
		}catch(Exception ex) {
			logger.error("Cause : " + ex.getCause());
		}
	}
}
