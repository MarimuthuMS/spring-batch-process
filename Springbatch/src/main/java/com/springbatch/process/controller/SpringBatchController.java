package com.springbatch.process.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/load")
public class SpringBatchController {
	
	Logger logger = LoggerFactory.getLogger(SpringBatchController.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;
    // Rest end point for calling job manually.
    @GetMapping
    @RequestMapping("/loadJob")
    public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        try {
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters);

        while (jobExecution.isRunning()) {
       	 logger.info(".........");
        }
   	 	logger.info("jobExecution.getStatus()-->"+jobExecution.getStatus());
        return jobExecution.getStatus();
        }
        catch(Exception ex) {
        	logger.error("load job Cause : " + ex.getCause());
        }
		return null;
    }
}