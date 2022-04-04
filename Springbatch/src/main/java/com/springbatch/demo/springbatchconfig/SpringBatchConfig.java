package com.springbatch.demo.springbatchconfig;


import java.io.IOException;
import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.springbatch.demo.aop.AspectBatch;
import com.springbatch.demo.model.Employee;




@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	
	
	 @Bean
	    public Job job(JobBuilderFactory jobBuilderFactory,
	                   StepBuilderFactory stepBuilderFactory,
	                   ItemReader<Employee> itemReader,
	                   ItemProcessor<Employee, Employee> itemProcessor,
	                   ItemWriter<Employee> itemWriter
	    ) {

	        Step step = stepBuilderFactory.get("ETL-file-load")
	                .<Employee, Employee>chunk(100)
	                .reader(multiResourceItemreader())
	                .processor(itemProcessor)
	                .writer(itemWriter)
	                .build();


	        return jobBuilderFactory.get("ETL-Load")
	                .incrementer(new RunIdIncrementer())
	                .start(step)
	                .build();
	    }
	 
		@Bean
		public MultiResourceItemReader<Employee> multiResourceItemreader() {
			MultiResourceItemReader<Employee> reader = new MultiResourceItemReader<>();
			 Resource[] resources = null;
			    ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();   
			    try {
			        resources = patternResolver.getResources("/input/*.csv");
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			reader.setDelegate(itemReader());
			reader.setResources(resources);
			return reader;
		}
		
	    @Bean
	    public FlatFileItemReader<Employee> itemReader() {
	    	   
	        FlatFileItemReader<Employee> flatFileItemReader = new FlatFileItemReader<>();
	       // flatFileItemReader.setResource(new ClassPathResource("input/employee1.csv"));
	        flatFileItemReader.setName("CSV-Reader");
	        flatFileItemReader.setLinesToSkip(1);
	        flatFileItemReader.setLineMapper(lineMapper());
	        return flatFileItemReader;
	    }

	    @Bean
	    public LineMapper<Employee> lineMapper() {

	        DefaultLineMapper<Employee> defaultLineMapper = new DefaultLineMapper<>();
	        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

	        lineTokenizer.setDelimiter(",");
	        lineTokenizer.setStrict(false);
	        lineTokenizer.setNames("empId", "name", "dob", "rating");

	        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<Employee>();
	        fieldSetMapper.setTargetType(Employee.class);
	        DefaultConversionService conversionService = new DefaultConversionService();
    	    conversionService.addConverter(new Converter<String, Date>() { // java.sql.Date
    	        @Override
    	        public Date convert(String s) {
    	            return Date.valueOf(s);
    	        }
    	    });
    	    fieldSetMapper.setConversionService(conversionService);
	        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
	        defaultLineMapper.setLineTokenizer(lineTokenizer);
	        return defaultLineMapper;
	    }
	
	
	
	

}
