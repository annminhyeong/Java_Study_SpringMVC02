package kr.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import kr.board.entity.Board;

//@Mapper : mybatis API가 가지고 있음
@Mapper
public interface BoardMapper {
	//게시판 조회
	public List<Board> getLists();
	
	//게시판 등록
	public void boardInsert(Board vo);
	
	//게시판 상세보기
	public Board boardContent(int idx);
	
	//게시판 삭제하기
	public void boardDelete(int idx);
	
	//게시판 수정하기
	public void boardUpdate(Board vo);
	
	//게시판 조회수
	@Update("update myboard set count=count+1 where idx = #{idx}")
	public void boardCount(int idx);
}
