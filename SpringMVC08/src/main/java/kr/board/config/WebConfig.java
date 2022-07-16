package kr.board.config;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//web.xml : 스프링이 시작하면 제일먼저 실행되는 xml파일
//필터와 root-context.xml 파일과 servlet-context.xml를 매핑하는 역할을 했음
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer{
	
	//인코딩(filter 대용) 설정 역할을 함
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		
		//필터 객체 배열를 생성 (클래스들의 환경세팅이 여려개일 수 있으므로 배열객체 생성)
		return new Filter[] {encodingFilter};
	}
	
	
	//RootConfig(root-context.xml 대용) 파일을 매핑하는 역할을 함  
	@Override
	protected Class<?>[] getRootConfigClasses() {
		
		//객체 배열를 생성 (클래스들의 환경세팅이 여려개일 수 있으므로 배열객체 생성)
		return new Class[] {RootConfig.class};
	}
	
	//ServletConfig(servlet-context.xml 대용) 파일을 매핑하는 역할을 함  
	@Override
	protected Class<?>[] getServletConfigClasses() {
		
		//객체 배열를 생성 (클래스들의 환경세팅이 여려개일 수 있으므로 배열객체 생성)
		return new Class[] {ServletConfig.class};
	}
	
	//RootConfig파일과 매핑하는 역할을 함
	@Override
	protected String[] getServletMappings() {
		
		//객체 배열를 생성 (클래스들의 경로가 여려개 일 수 있으므로 배열객체 생성)
		return new String[] {"/"};
	}
}
