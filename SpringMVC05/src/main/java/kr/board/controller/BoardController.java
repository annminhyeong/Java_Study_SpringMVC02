package kr.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.board.entity.Board;
import kr.board.mapper.BoardMapper;

//Spring에서 지원해주는 FrontController를 거쳐 POJO Controller로 옴

//@Controller 대신 @Component로 사용해도 상관없음
@Controller
public class BoardController {
	
	@Autowired
	private BoardMapper boardmapper;
	
	//클라이언트로부터 /boardList.do 요청이 오면 
	//클래스 내부의 HandlerMapping 클래스가 처리해줌
	//get과 post요청 둘다받음
	@RequestMapping("/boardList.do")
	public String boardList(Model model) {
		
		List<Board> list = boardmapper.getLists();
		model.addAttribute("list", list);
		
		//view의 경로(WEB-INF/views/boardList.jsp)를 클라이언트에게 보여줌
		//viewResolver클래스가(servlet-context-xml)가 자동으로 접두사, 접미사를 붙여서 forward 해줌
		return "boardList";
	}
	
	//get방식으로 서버에서 요청할때 getMapping으로 받음
	@GetMapping("/boardForm.do")
	public String boardForm() {
		//viewResovler가 전체경로를 만들어줌 (WEB-INF/views/boardForm.jsp) -> forward
		return "boardForm";
	}
	
	//post방식으로 서버에서 요청할때 getMapping으로 받음
	//BoardForm에서 보낸 데이터를 Board vo로 받음
	//단, Board클래스에 BoardForm에서 보낸 name이름이 존재해야함
	@PostMapping("/boardInsert.do")
	private String boardInsert(Board vo) {
		boardmapper.boardInsert(vo);
		
		//redirect 방식
		return "redirect:/boardList.do";
	}
	
}
