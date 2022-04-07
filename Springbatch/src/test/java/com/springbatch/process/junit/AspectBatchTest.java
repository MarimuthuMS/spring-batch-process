package com.springbatch.process.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AopTestUtils;

import com.springbatch.process.aop.AspectBatch;

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
	    @Autowired
	    private AspectBatch aspectBatch;

	    @Test
	    @DisplayName("Test_Positive_Small_Number")
	    public void testPositiveSmallNumber() throws Throwable {
	    	//aspectBatch.intercept(proceedingJoinPoint, 11);
	        // 'proceed()' is called exactly once
	        verify(proceedingJoinPoint, times(1)).proceed();
	        // 'proceed(Object[])' is never called
	        verify(proceedingJoinPoint, never()).proceed(null);
	    }

	    @Test
	    @DisplayName("Test_Negative_Number")
	    public void testNegativeNumber() throws Throwable {
	    	//aspectBatch.intercept(proceedingJoinPoint, -22);
	        // 'proceed()' is never called
	        verify(proceedingJoinPoint, never()).proceed();
	        // 'proceed(Object[])' is called exactly once
	        verify(proceedingJoinPoint, times(1)).proceed(new Object[] { 22 });
	    }

	    @Test
	    @DisplayName("Test_Positive_Number")
	    public void testPositiveLargeNumber() throws Throwable {
	    	//aspectBatch.intercept(proceedingJoinPoint, 333);
	    }
}
