package kr.board.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

//빈(환경설정) 등록을 위한 어노테이션
@Configuration

//@Mapper 어노테이션을 스캔해 bean객체에 등록해줌
@MapperScan(basePackages = {"kr.board.mapper"})

//src/main/resources폴더에서 properties 파일을 불러오는 어노테이션
@PropertySource({"classpath:persistence-mysql.properties"})
public class RootConfig {
	
	//필요한 의존 객체의 타입(생성자, setter, 필드)에 해당하는 빈을 찾아 의존성 주입(객체를 생성하지 않고)한다.
	@Autowired
	//properties파일을 참조할려면 Environment가 필요함
	private Environment env;
	
	//bean :Spring IoC 컨테이너가 관리하는 자바 객체
	//@Bean : Spring 컨테이너에 bean객체를 생성
	@Bean
	
	//Hikari API를 이용한 mysql 스프링 연동
	public DataSource myDataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(env.getProperty("jdbc.driver"));
		hikariConfig.setJdbcUrl(env.getProperty("jdbc.url"));
		hikariConfig.setUsername(env.getProperty("jdbc.user"));
		hikariConfig.setPassword(env.getProperty("jdbc.password"));
		
		HikariDataSource myDataSource = new HikariDataSource(hikariConfig);
		
		return myDataSource;
	}
	
	//bean :Spring IoC 컨테이너가 관리하는 자바 객체
	//@Bean : Spring 컨테이너에 bean객체를 생성
	@Bean
	
	//여러개의 데이터베이스 여러개의 커넥션 풀 객체를 만든다.
	public SqlSessionFactory sessionFactory() throws Exception{
		
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(myDataSource());
		
		return (SqlSessionFactory)sessionFactory.getObject();
	}
}
