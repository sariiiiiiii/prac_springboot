package com.board.aop;

import java.util.Collections;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

// AOP를 이용해 트랜잭션을 설정하면 새로운 클래스나 메소드가 추가될 때 따로 어노테이션을 붙이지 않아도 자동적으로 트랜잭션이 처리됨.
// 따라서 어노테이션 누락이나 잘못된 사용에 따른 문제를 미연에 방지할 수 있다.


@Configuration
public class TransactionAspect {
	
	private static final String AOP_TRANSACTION_METHOD_NAME = "*"; // 트랜잭션을 설정할 때 사용되는 설정값을 상수로 선언
	private static final String AOP_TRANSCTION_EXPRESSION = "execution(* com.board..service.*Impl.*(..))"; // 포인트컷 비즈니스로직을 수행하는 모든 serviceImpl클래스의 모든 메소드를 의미	
	
	@Autowired
	private TransactionManager transactionManager; // DBConfiguration 클래스에 Bean으로 등록한 PlatformTransactionManager 객체
	
	@Bean
	public TransactionInterceptor transactionAdvice() {
		MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();
		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
		transactionAttribute.setName(AOP_TRANSACTION_METHOD_NAME); // 트랜잭션 이름을 설정함. 트랜잭션 모니터에서 트랜잭션 이름을 확인할 수 있음.
		
		transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		// 트랜잭션에서 롤백하는 룰을 설정. 현재는 예외가 발생했을 때 롤백이 되도록 지정한 상태. 
		// Exception.class를 룰로 등록하면, 자바의 모든 예외는 Exception 클래스를 상속받기 때문에 어떠한 예외가 발생하더라도 롤백이 수행됨
		
		source.setTransactionAttribute(transactionAttribute);
		
		return new TransactionInterceptor(transactionManager, source);
	}
	
	@Bean
	public Advisor transactionAdviceAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(); // pointCut을 설정함, EXPRESSION에 지정한 ServiceImpl 클래스의 모든 메소드를 대상으로 설정
		pointcut.setExpression(AOP_TRANSCTION_EXPRESSION);
		
		return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
	}
	
}