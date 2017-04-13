package com.mayi.seller.manager.aop;

import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mayi.api.domain.basic.ServiceResult;

public class RollBackAspect {
	
	private static Logger logger = Logger.getLogger(RollBackAspect.class);
	
	public void doAfter(JoinPoint jp) {
		if (logger.isInfoEnabled()) {
			logger.info("log Ending method: "
					+ jp.getTarget().getClass().getName() + "."
					+ jp.getSignature().getName());
		}
    }  
	
	@Resource
	private DataSourceTransactionManager transactionManager;
    
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long time = System.currentTimeMillis();
        Object retVal = null;

        //获取事务
        DefaultTransactionDefinition def =new DefaultTransactionDefinition();
        TransactionStatus status=transactionManager.getTransaction(def);
        String transactionId = UUID.randomUUID().toString();
        try {
        	logger.debug("transactionId："+transactionId+",transaction start");
        	retVal = pjp.proceed();
        	// 如果返回false，则事务不提交
        	if (retVal instanceof ServiceResult<?>) {
        		ServiceResult<?> serviceResult = (ServiceResult<?>) retVal;
        		if (!serviceResult.isOk()) {
        			transactionManager.rollback(status);
        			logger.error("transactionId："+transactionId+",transaction rollback !ServiceResult return false----------------------");
        			return retVal;
        		}
        	}
        	//事务执行
        	transactionManager.commit(status);
        	logger.debug("transactionId："+transactionId+",transaction commit");
        } 
        catch (RuntimeException e) {
            if (retVal == null) {
            	ServiceResult<Object> result  = new ServiceResult<Object>();
            	result.setMsgCode(e.getMessage());
            	retVal = result;
            }
        	//事务回滚
        	transactionManager.rollback(status);
        	logger.error("transactionId："+transactionId+",transaction rollback !RuntimeException----------------------",e);
        } catch (Exception e) {
        	//事务回滚
        	transactionManager.rollback(status);
        	logger.error("transactionId："+transactionId+",transaction rollback !update----------------------",e);
        }
         
        time = System.currentTimeMillis() - time;
        if (logger.isDebugEnabled()) {
        	logger.debug("method " + pjp.getTarget().getClass().getName()
        		+ "." + pjp.getSignature().getName()
        		+ " elapsed time:"+time);
        }
        
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
    		logger.info("method " + jp.getTarget().getClass().getName()
    				+ "." + jp.getSignature().getName() + " throw exception");
    		logger.info(ex.getMessage());
		}
    }  
}
