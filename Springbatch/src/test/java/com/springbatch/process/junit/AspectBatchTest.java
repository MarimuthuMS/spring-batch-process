package com.springbatch.process.junit;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.springbatch.process.aop.AspectBatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AspectBatchTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;

	@Mock
	private JoinPoint joinPoint;

	@Autowired
	private AspectBatchService aspectBatchService;

	@Test
	@DisplayName("Test_Aspect_Event")
	public void testAspectEvent() {
		try {
			aspectBatchService.callFunctionWithArgs("AOP Unit Testing");
			aspectBatchService.callFuncWithNoArgs();
			aspectBatchService.returnTypeMethod();
		} catch (Exception ex) {
			logger.error("Cause : " + ex.getCause());
		}

	}

}
