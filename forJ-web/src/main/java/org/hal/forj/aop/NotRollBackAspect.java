package org.hal.forj.aop;


import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.mayi.api.domain.basic.ServiceResult;

public class NotRollBackAspect {

	private static Logger logger = Logger.getLogger(NotRollBackAspect.class);

	public void doAfter(JoinPoint jp) {
		if (logger.isInfoEnabled()) {
			logger.info("log Ending method: " + jp.getTarget().getClass().getName()
				+ "." + jp.getSignature().getName());
		}
	}

	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long time = System.currentTimeMillis();
		Object retVal = null;

		try {
			retVal = pjp.proceed();
		} catch (Exception e) {
			if (retVal == null) {
				ServiceResult<Object> result = new ServiceResult<Object>();
				result.setMsgCode(e.getMessage());
				retVal = result;
			}
			logger.error("Exception----------------------", e);
		}

		time = System.currentTimeMillis() - time;

		if (retVal == null) {
			retVal = new ServiceResult<Object>();
		}
		return retVal;
	}

	public void doBefore(JoinPoint jp) {
		if (logger.isInfoEnabled()) {
			logger.info("log Begining method: "
				+ jp.getTarget().getClass().getName() + "."
				+ jp.getSignature().getName());
		}
	}

	public void doThrowing(JoinPoint jp, Throwable ex) {
		if (logger.isInfoEnabled()) {
			logger.info("method " + jp.getTarget().getClass().getName() + "."
				+ jp.getSignature().getName() + " throw exception");
			logger.info(ex.getMessage());
		}
	}
}
