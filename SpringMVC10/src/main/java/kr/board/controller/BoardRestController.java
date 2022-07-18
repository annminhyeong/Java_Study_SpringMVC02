package kr.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.board.entity.Board;
import kr.board.mapper.BoardMapper;




// /board로 오는 모든 요청을 처리하겠다는 뜻
@RequestMapping("/board")
//@RestConroller는 모든 메서드들은 @ResponseBody 생략가능
//ajax(비동기) 통신(json 응답)을 할때 사용하는 컨트롤러
@RestController
public class BoardRestController {
	
	@Autowired
	private BoardMapper boardmapper;
	
	
	//JSON 형식으로 변환해서 리턴할려면 @ResponseBody 붙이기 
	//(Jackson-Databind API를 pom.xml에 추가하기)
	//@RequestMapping("/boardList.do")
	//@ResponseBody
	@GetMapping("/all")
	public List<Board> boardList(){
		List<Board> list = boardmapper.getLists();
		return list;
	}
	
	//@RequestMapping : 다시 결과를 ajax에게 넘김 	
	//@RequestMapping("boardInsert.do")
	//@ResponseBody
	@PostMapping("/new")
	public void boardInsert(Board vo) {
		boardmapper.boardInsert(vo);
	}
	
	//@RequestParam("idx")과 같은 파라미터명을 사용하면 생략가능
	//@RequestMapping("boardDelete.do")
	//@ResponseBody
	@DeleteMapping("/{idx}")
	public void boardDelete(@PathVariable("idx") int idx) {
		boardmapper.boardDelete(idx);
	}
	
	//@RequestMapping("boardUpdate.do")
	//@ResponseBody
	//@RequestBody : json형식으로 받기
	@PutMapping("/update")
	public void boardUpdate(@RequestBody Board vo) {		
		boardmapper.boardUpdate(vo);
	}
	
	//@RequestMapping("boardContent.do")
	//@ResponseBody
	@GetMapping("/{idx}")
	public Board boardContent(@PathVariable("idx") int idx) {
		Board vo = boardmapper.boardContent(idx);
		return vo;
	}
	
	//@RequestMapping("boardCount.do")
	//@ResponseBody
	@PutMapping("/count/{idx}")
	public Board boardCount(@PathVariable("idx") int idx) {		
		boardmapper.boardCount(idx);
		Board vo = boardmapper.boardContent(idx);
		return vo;
	}
}
