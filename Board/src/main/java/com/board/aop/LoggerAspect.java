package com.board.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component 
// 스프링 컨테이너에 Bean객체로 등록하기 위한 어노테이션
// @Bean은 개발자가 제어할 수 없는 외부 라이브러리를 @Bean으로 등록할 때 사용하고,
// @Component는 개발자가 직접 정의한 클래스를 Bean으로 등록할 때 사용한다.
@Aspect
// AOP 기능을 하는 클래스에 클래스레벨을 지정하는 어노테이션
public class LoggerAspect {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Around("execution(* com.board..controller.*Controller.*(..)) or execution(* com.board..service.*Impl.*(..)) or execution(* com.board..mapper.*Mapper.*(..))")
	// 어드바이스의 5가지 기능 중 한가지로 메소드의 호출 자체를 제어할 수 있기 때문에 어드바이스 중 가장 강력한 기능을 가졌다고 할 수 있다.
	// @Before, @AfterReturning, @AfterThrowing, @After, @Around (https://congsong.tistory.com/25?category=749196)
	public Object pringLog(ProceedingJoinPoint joinPoint) throws Throwable {
		// 메소드의 정보를 가지고 있는 Signature 객체에 담겨 있는 파일명을 포함한 전체 패키지 경로를 name에 담아
		// 어떤 클래스의 어떤 메소드를 호출하는지를 로그에 출력
		
		String type = "";
		String name = joinPoint.getSignature().getDeclaringTypeName();
		
		if(name.contains("Controller") == true){
			type = "Controller ===> ";
		}else if(name.contains("Service") == true) {
			type = "Service ===>";
		}else if(name.contains("Mapper") == true) {
			type = "Mapper ===>";
		}
		
		logger.debug(type + name + "." + joinPoint.getSignature().getName() + "()"); // 실행되는 객체에 대한 메소드를 가지고 옴
		
		return joinPoint.proceed();
		
	}

}
