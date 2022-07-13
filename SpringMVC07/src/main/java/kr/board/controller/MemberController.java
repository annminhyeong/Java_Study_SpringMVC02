package kr.board.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.board.entity.Member;
import kr.board.mapper.MemberMapper;

@Controller
public class MemberController {
	
	@Autowired
	private MemberMapper membermapper;
	
	@RequestMapping("/memJoin.do")
	public String memJoin() {
		return "member/join";
	}
	
	@RequestMapping("/memRegisterCheck.do")
	public @ResponseBody int memRegisterCheck(@RequestParam("memID") String memID) {
		
		 Member m = membermapper.registerCheck(memID);
		 
		 //아이디 사용불가능
		 if(m != null || memID.equals("")) {
			 return 0;
		 }
		 //아이디 사용가능
		 return 1;
	}
	
	//RedirectAttributes : jsp로 전송하는게 아니므로 객체바인딩하는게 불가능함, 이때 객체바인딩하고 싶을때 사용 
	@RequestMapping("/memRegister.do")
	public String memRegister(RedirectAttributes rAttr, HttpSession session,
				              Member m, String memPassword1, String memPassword2) {
		if(m.getMemID()==null       || m.getMemID().equals("")       ||
		   memPassword1==null       || memPassword1.equals("")       ||
		   memPassword2==null       || memPassword2.equals("")       ||
		   m.getMemName()==null     || m.getMemName().equals("")     ||
		   m.getMemGender()==null   || m.getMemGender().equals("")   ||
		   m.getMemEmail()==null    || m.getMemEmail().equals("")    ||
		   m.getMemAge()==0
		){	
		   //객체바인딩을 한번만 리다이렉트해서 전달함
		   rAttr.addFlashAttribute("msgType","실패 메세지");
		   rAttr.addFlashAttribute("msg","모든 내용을 입력하세요.");
		   return "redirect:/memJoin.do";
		}
		
		if(!memPassword1.equals(memPassword2)) {
		   rAttr.addFlashAttribute("msgType","실패 메세지");
		   rAttr.addFlashAttribute("msg","비밀번호가 서로 다릅니다.");
		   return "redirect:/memJoin.do";
		}
		//사진없음
		m.setMemProfile("");
		
		int result = membermapper.register(m);
		if(result == 1) {
		   rAttr.addFlashAttribute("msgType","성공 메세지");
		   rAttr.addFlashAttribute("msg","회원가입에 성공했습니다.");
		   
		   //회원가입이 성공하면 로그인처리하기
		   session.setAttribute("mvo", m);
		   return "redirect:/";
		}else {
		   rAttr.addFlashAttribute("msgType","실패 메세지");
		   rAttr.addFlashAttribute("msg","이미 존재하는 회원입니다.");
		   return "redirect:/memJoin.do";
		}
	}
	
}//class
