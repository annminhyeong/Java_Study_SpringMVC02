package kr.board.entity;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

//3. 인증 후 사용자 정보 저장하기
@Data
@EqualsAndHashCode(callSuper=false)
public class MemberUser extends User{ //User의 부모클래스는 UserDetails임

	private static final long serialVersionUID = 1L;
	
	private Member member;
	
	public MemberUser(Member mvo) {
		super(mvo.getMemID(), mvo.getMemPassword(),
			//권한 List<AuthVO>는 스프링에서 제공해주는 Collection<SimpleGrantAutority>로 바꿔서 저장해야함
			//List를 스트림을 변환한 다음 SimpleGrantedAuthority 권한객체를 생성하고 다시 리스트로 다시 변환한다.
			  mvo.getAuthList()
			  .stream().map(auth-> new SimpleGrantedAuthority(auth.getAuth()))
			  .collect(Collectors.toList()));
		
		this.member = mvo;
	}
	
}
