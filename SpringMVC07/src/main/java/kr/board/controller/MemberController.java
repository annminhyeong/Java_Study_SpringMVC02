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
	
	@RequestMapping("/memJoinForm.do")
	public String memJoin() {
		return "member/memJoinForm";
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
		   return "redirect:/memJoinForm.do";
		}
		
		if(!memPassword1.equals(memPassword2)) {
		   rAttr.addFlashAttribute("msgType","실패 메세지");
		   rAttr.addFlashAttribute("msg","비밀번호가 서로 다릅니다.");
		   return "redirect:/memJoinForm.do";
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
		   return "redirect:/memJoinForm.do";
		}
	}
	
	@RequestMapping("/memLogout.do")
	public String memLogout(HttpSession session) {
		//세션 전부 끊기
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping("/memLoginForm.do")
	public String memLoginForm() {
		return "member/memLoginForm";
	}
	
	@RequestMapping("/memLogin.do")
	public String memLogin(Member m, RedirectAttributes rAttr, HttpSession session) {
		if(m.getMemID()==null       || m.getMemID().equals("") ||
		   m.getMemPassword()==null || m.getMemPassword().equals("")) {
			rAttr.addFlashAttribute("msgType", "실패 메세지");
			rAttr.addFlashAttribute("msg", "아이디와 비밀번호를 입력해주세요.");
			return "redirect:/memLoginForm.do";
		}
		Member mvo = membermapper.memLogin(m);
		if(mvo != null) {
			rAttr.addFlashAttribute("msgType", "성공 메세지");
			rAttr.addFlashAttribute("msg", mvo.getMemID()+"님 로그인에 성공했습니다.");
			session.setAttribute("mvo", mvo);
			return "redirect:/";
		}else {
			rAttr.addFlashAttribute("msgType", "실패 메세지");
			rAttr.addFlashAttribute("msg", "아이디와 비밀번호가 일치하지 않습니다.");
			return "redirect:/memLoginForm.do";
		}
	}
	
	@RequestMapping("/memUpdateForm.do")
	public String memUpdateForm() {
		return "member/memUpdateForm";
	}
	
	@RequestMapping("/memUpdate.do")
	public String memUpdate(Member m, RedirectAttributes rAttr, HttpSession session,
							String memPassword1,String memPassword2) {
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
		   return "redirect:/memUpdateForm.do";
		}
		
		if(!memPassword1.equals(memPassword2)) {
		   rAttr.addFlashAttribute("msgType","실패 메세지");
		   rAttr.addFlashAttribute("msg","비밀번호가 서로 다릅니다.");
		   return "redirect:/memUpdateForm.do";
		}
		System.out.println("나이:" + m.getMemAge());
		int result = membermapper.memUpdate(m);
		if(result == 1) {
		   rAttr.addFlashAttribute("msgType","성공 메세지");
		   rAttr.addFlashAttribute("msg","회원정보수정에 성공했습니다.");
		   //회원정보 수정이 성공하면 로그인처리하기
		   session.setAttribute("mvo", m);
		   return "redirect:/";
		}else {
		   rAttr.addFlashAttribute("msgType","실패 메세지");
		   rAttr.addFlashAttribute("msg","회원정보수정에 실패했습니다.");
		   return "redirect:/memUpdateForm.do";
		}
	}
	
	@RequestMapping("/memImageForm.do")
	public String memImageForm() {
		return "member/memImageForm";
	}
	
}//class
