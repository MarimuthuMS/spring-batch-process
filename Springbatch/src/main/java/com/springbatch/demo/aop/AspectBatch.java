package com.springbatch.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class AspectBatch {
	Logger logger = LoggerFactory.getLogger(AspectBatch.class);
	
	@Pointcut("execution(* com.springbatch.demo.*.*.*(..))")
	public void loggingMethod() {

	}

	@Before("loggingMethod()")
	public void beforeEvent(JoinPoint joinPoint) throws Throwable {
		 logger.info("Process Started Method Name....."+joinPoint.getSignature());
		 logger.info("Process Started  Class Name....."+joinPoint.getTarget().getClass());
		 Object[] array =joinPoint.getArgs();
		 logger.info("Process Started Arguments....."+array.toString());

	}
	
	@After("loggingMethod()")
	public void AfterEvent(JoinPoint joinPoint) throws Throwable {

		Logger logger = LoggerFactory.getLogger(AspectBatch.class);
		
		 logger.info("Process Ended Method Name....."+joinPoint.getSignature());
		 logger.info("Process Ended Class Name....."+joinPoint.getTarget().getClass());
		 Object[] array =joinPoint.getArgs();
		 logger.info("Process Ended Arguments....."+array.toString());		

	}
	
	@Around("loggingMethod()")
    public Object executionTime(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object object = point.proceed();
        long endtime = System.currentTimeMillis();
        logger.info("Class Name: "+ point.getSignature().getDeclaringTypeName() +". Method Name: "+ point.getSignature().getName() + ". Time taken for Execution is : " + (endtime-startTime) +"ms");
        return object;
    }
	

    @AfterReturning(pointcut = "loggingMethod()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String returnValue = this.getValue(result);
        logger.debug("Method Return value : " + returnValue);
    }
   
    @AfterThrowing(pointcut = "loggingMethod()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
    	logger.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
    	logger.error("Cause : " + exception.getCause());
    }
    
    private String getValue(Object result) {
        String returnValue = null;
        if (null != result) {
            if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
                returnValue = result.toString();
            } else {
                returnValue = result.toString();
            }
        }
        return returnValue;
    }

}
