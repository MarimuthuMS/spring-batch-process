package com.springbatch.process.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AspectBatchService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void callFunctionWithArgs(String name) {
		try {
			logger.info("Process Started ....."+name);
		} catch (Exception ex) {
			logger.error("Cause : " + ex.getCause());
		}
	}

	public void callFuncWithNoArgs() {
		try {
			logger.info("Process Started ....." );
		} catch (Exception ex) {
			logger.error("Cause : " + ex.getCause());
		}
	}

	public String returnTypeMethod() {
		try {
			logger.info("Process Started ....");
	
		} catch (Exception ex) {
			logger.error("Cause : " + ex.getCause());
		}
		return null;
	}

}
