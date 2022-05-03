package com.board.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@EnableTransactionManagement // 스프링에서 제어하는 어노테이션 기반 트랜잭션을 활성화 AOP기반을 사용할거라서 쓰진 않을거지만 경험할겸 추가해놓도록 하자
@Configuration // 스프링은 @Configuration이 지정된 클래스를 자바기반의 설정파일로 인식
@PropertySource("classpath:/application.properties") // 해당 클래스에서 참조할 properties 파일의 위치를 설정
public class DBConfiguration {

	@Autowired // @Bean으로 등록된 인스턴스를 클래스에 주입
	private ApplicationContext applicationContext; // ApplicationContext는 스프링컨테이너 중 하나, Bean의 생성과 관계 생명주기 등 관리

	@Bean // Configuration클래스의 메소드 레벨에만 지정이 가능 , @Bean이 지정된 객체는 컨테이너에 의해 관리되는 Bean으로 등록된다 인자로 몇가지 속성 지정할 수 있다.
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	// @PropertySource에 지정된 파일(Application.properties)에서 prefix(접두사, 머리)에 해당하는 spring.datasource.hikari로 시작하는 설정을 모두 읽어 들여 해당 메소드에 매핑
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig());
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception { 
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**/*Mapper.xml"));
		
		// setTypeAliasesPackage가 domain 패키지로 잡혀있기 때문에 BoardMapper XML에서 파라미터로 지정한 Criteria를 인식하지 못하는 문제가 발생한다
		// 따라서 setTypeAliasesPackage를 com.board.*로 수정
		/* factoryBean.setTypeAliasesPackage("com.board.domain"); */
		factoryBean.setTypeAliasesPackage("com.board.*");
		
		factoryBean.setConfiguration(mybatisConfg());
		return factoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}

	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	public org.apache.ibatis.session.Configuration mybatisConfg() {
		return new org.apache.ibatis.session.Configuration();
	}
	
	@Bean // 스프링에서 제공하는 트랜잭션 매니저를 Bean으로 등록
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

}