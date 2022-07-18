package kr.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.board.entity.AuthVO;
import kr.board.entity.Member;

//@Mapper : mybatis API가 가지고 있음
@Mapper
public interface MemberMapper {
	
	//아이디 중복체크
	public Member registerCheck(String memID);
	
	//회원가입 (성공1, 실패0)
	public int register(Member m);
	
	//로그인
	public Member memLogin(Member mvo);
	
	//회원정부 수정
	public int memUpdate(Member mvo);
	
	//기존에 저장되어 있는 파일명 검색하기
	public Member getMember(String memID);
	
	//프로필 업데이트
	public void memProfileUpdate(Member m);
	
	//권한테이블 저장
	public void authInsert(AuthVO saveVO);
	
	//권한 삭제
	public void authDelete(String memID);
}
