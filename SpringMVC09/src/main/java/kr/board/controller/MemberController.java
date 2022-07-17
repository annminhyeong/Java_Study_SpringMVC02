package kr.board.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.board.entity.AuthVO;
import kr.board.entity.Member;
import kr.board.mapper.MemberMapper;

@Controller
public class MemberController {
	
	@Autowired
	private MemberMapper membermapper;
	
	//의존성 주입(객체를 생성하지 않고 사용)
	@Autowired
	private PasswordEncoder pwEncoder;
	
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
		   m.getMemAge()==0         || m.getAuthList().size() == 0
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
		
		//비밀번호 암호화하기
		String encyptPw = pwEncoder.encode(m.getMemPassword());
		m.setMemPassword(encyptPw);
		
		int result = membermapper.register(m);
		
		//회원가입 성공하면
		if(result == 1) {
		   //클라이언트가 보낸 권한들을 리스트로 저장
		   List<AuthVO> list = m.getAuthList();
		   
		   for(AuthVO authVO : list) {
			   
			   //만약 권한이 있다면
			   if(authVO.getAuth() != null) {
				   //권한테이블 객체 생성
				   AuthVO saveVO = new AuthVO();
				   //클라이언트가 보내온 아이디를 권한테이블에 넣기
				   saveVO.setMemID(m.getMemID());
				   //클라이언트가 보내온 권한를 권한테이블에 넣기
				   saveVO.setAuth(authVO.getAuth());
				   membermapper.authInsert(saveVO);
			   }
		   }
		   
		   rAttr.addFlashAttribute("msgType","성공 메세지");
		   rAttr.addFlashAttribute("msg","회원가입에 성공했습니다.");
		   
		   //회원정보 + 권한정보
		   Member mvo = membermapper.getMember(m.getMemID());
		   System.out.println(mvo);
		   //회원가입이 성공하면 로그인처리하기
		   session.setAttribute("mvo", mvo);
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
		
		//비밀번호 일치확인
		//PasswordEncoder matches(p1, p2)는 p1(입력받은 값)과 p2(암호화된 값)을 비교해줌
		if(mvo != null && pwEncoder.matches(m.getMemPassword(), mvo.getMemPassword())) {
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
		
		int result = membermapper.memUpdate(m);
		if(result == 1) {
		   rAttr.addFlashAttribute("msgType","성공 메세지");
		   rAttr.addFlashAttribute("msg","회원정보수정에 성공했습니다.");
		   Member mvo = membermapper.getMember(m.getMemID());
		   //회원정보 수정이 성공하면 로그인처리하기
		   session.setAttribute("mvo", mvo);
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
	
	@RequestMapping("/memImageUpdate.do")
	public String memImageUpdate(HttpServletRequest request, RedirectAttributes rAttr, HttpSession session) {
		//파일 업로드 API(cos.jar)
		MultipartRequest multi = null;
		
		int fileMaxSize = 10*1024*1024; //10MB
		
		//파일 업로드 경로
		String savePath = request.getSession().getServletContext().getRealPath("resources/upload");
		
		try {
			//upload폴더에 파일명을 저장
			//1번째 인자 : 클라이언트에서 받아온 name 속성을 받기위해서 request객체를 저장
			//2번째 인자 : 파일을 저장할 경로
			//3번째 인자 : 최대용량 설정
			//4번째 인자 : 인코딩 설정
			//5번째 인자 : 파일 중복시, 파일이름 rename
			multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
		} catch (Exception e) {
			e.printStackTrace();
			rAttr.addFlashAttribute("msgType","실패 메세지");
			rAttr.addFlashAttribute("msg","파일의 크기는 10MB를 넘길 수 없습니다.");
			return "redirect:/memImageForm.do";
		}
		
		
		String memID = multi.getParameter("memID");
		String newProfile = "";
		
		//클라이언트에서 name속성으로 보낸 memProfile를 파일객체로 생성해서 저장
		File file = multi.getFile("memProfile");
		
		//클라이언트에서 name속성으로 보낸 memProfile 있으면
		if(file != null) {
			//업로드가 된 상태(.png, .jpg, .gif)
			
			// .다음부터 끝까지 잘라내서 저장 (확장자)
			String ext = file.getName().substring(file.getName().lastIndexOf(".")+1);
			
			//파일 확장자 체크
			if(ext.equalsIgnoreCase("PNG") || ext.equalsIgnoreCase("JPG") || ext.equalsIgnoreCase("GIF")) {
				//DB에서 검색해 이미지이름를 저장
				String oldProfile = membermapper.getMember(memID).getMemProfile();
				
				//DB에서 가져온프로필 이름를 upload폴더에 저장
				File oldFile = new File(savePath+"/"+oldProfile);
				
				//만약 기존 파일이 존재하면 기존파일은 삭제
				if(oldFile.exists()) {
					oldFile.delete();
				}
				//클라이언트가 보낸 파일이름저장
				newProfile = file.getName();
			}else {//이미지파일이 아니면 파일 삭제
				if(file.exists()) {
					file.delete();
				}
				rAttr.addFlashAttribute("msgType","실패 메세지");
				rAttr.addFlashAttribute("msg","이미지만 업로드 가능합니다.");
				return "redirect:/memImageForm.do";
			}
		}
		
		//클라이언트가 보낸 memID와 newProfile를 저장
		Member m = new Member();
		m.setMemID(memID);
		m.setMemProfile(newProfile);
		
		//이미지 업로드
		membermapper.memProfileUpdate(m);
		
		//DB에서 업데이트한 이미지가 저장된 Member객체 가져옴
		Member mvo = membermapper.getMember(memID);
		
		//세션을 새롭게 생성함
		session.setAttribute("mvo", mvo);
	    rAttr.addFlashAttribute("msgType","성공 메세지");
	    rAttr.addFlashAttribute("msg","이미지변경에 성공했습니다.");
		
		
		return "redirect:/";
	}
	
}//class













