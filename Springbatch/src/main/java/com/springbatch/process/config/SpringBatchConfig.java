package com.springbatch.process.config;


import java.io.FileNotFoundException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;


import com.springbatch.process.model.Employee;





@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	Logger logger = LoggerFactory.getLogger(SpringBatchConfig.class);
	@Value("${input.files.location}")
	private String csvFileLocation;
	 // creating job bean
	 @Bean
	    public Job job(JobBuilderFactory jobBuilderFactory,
	                   StepBuilderFactory stepBuilderFactory,
	                   ItemReader<Employee> itemReader,
	                   ItemProcessor<Employee, Employee> itemProcessor,
	                   ItemWriter<Employee> itemWriter
	    ) throws Exception {

	        Step step = stepBuilderFactory.get(SpringBatchConstant.SPRING_ETL_FILE_LOAD)
	                .<Employee, Employee>chunk(100)
	                .reader(multiResourceItemreader())
	                .processor(itemProcessor)
	                .writer(itemWriter)
	                .faultTolerant()
					.skipLimit(10)
					.skip(Exception.class)
					.noSkip(FileNotFoundException.class)
					.build();



	        return jobBuilderFactory.get(SpringBatchConstant.SPRING_ETL_LOAD)
	                .incrementer(new RunIdIncrementer())
	                .start(step)
	                .build();
	    }
	  // Multiple CSV Resource file Item Reader
		@Bean
		public MultiResourceItemReader<Employee> multiResourceItemreader() throws Exception  {
			try {
			MultiResourceItemReader<Employee> reader = new MultiResourceItemReader<>();
			 Resource[] resources = null;
			    ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();   
			    try {
			        resources = patternResolver.getResources(csvFileLocation);
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			reader.setDelegate(itemReader());
			reader.setResources(resources);
			return reader;
		}catch(Exception ex) {
    		logger.error("Multi Resource Item reader Cause : " + ex.getCause());
    	}
			return null;
		}
		// Single Resource file Item Reader and also skipping first line header
	    @Bean
	    public FlatFileItemReader<Employee> itemReader() throws Exception  {
	    	try {   
	        FlatFileItemReader<Employee> flatFileItemReader = new FlatFileItemReader<>();
	        flatFileItemReader.setName(SpringBatchConstant.SPRING_CSV_READER);
	        flatFileItemReader.setLinesToSkip(1);
	        flatFileItemReader.setLineMapper(lineMapper());
	        return flatFileItemReader;
	    	}catch(Exception ex) {
	    		logger.error("Item reader Cause : " + ex.getCause());
	    	}
			return null;
	    }
	    // CSV files field and object field mapping with date conversion
	    @Bean
	    public LineMapper<Employee> lineMapper() throws Exception  {
	    	try {
	        DefaultLineMapper<Employee> defaultLineMapper = new DefaultLineMapper<>();
	        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

	        lineTokenizer.setDelimiter(SpringBatchConstant.SPRING_DELIMITER);
	        lineTokenizer.setStrict(false);
	        lineTokenizer.setNames(SpringBatchConstant.SPRING_FIELD_ONE, SpringBatchConstant.SPRING_FIELD_TWO, SpringBatchConstant.SPRING_FIELD_THREE, SpringBatchConstant.SPRING_FIELD_FOUR);

	        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<Employee>();
	        fieldSetMapper.setTargetType(Employee.class);
	        DefaultConversionService conversionService = new DefaultConversionService();
    	    conversionService.addConverter(new Converter<String, Date>() { 
    	        @Override
    	        public Date convert(String s) {
    	            return Date.valueOf(s);
    	        }
    	    });
    	    fieldSetMapper.setConversionService(conversionService);
	        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
	        defaultLineMapper.setLineTokenizer(lineTokenizer);
	        return defaultLineMapper;
	        }catch(Exception ex) {
	    		logger.error("line Mapper Cause : " + ex.getCause());
	    	}
			return null;
	    }
}
