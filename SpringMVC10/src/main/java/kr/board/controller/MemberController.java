package kr.board.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.board.entity.AuthVO;
import kr.board.entity.Member;
import kr.board.entity.MemberUser;
import kr.board.mapper.MemberMapper;
import kr.board.security.MemberUserDetailsService;

@Controller
public class MemberController {
	
	@Autowired
	private MemberMapper membermapper;
	
	//의존성 주입(객체를 생성하지 않고 사용)
	@Autowired
	private PasswordEncoder pwEncoder;
	
	@Autowired
	MemberUserDetailsService memberUserDetailsService;
	
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
		   m.getMemAge()==0         || m.getAuthList().size()==0
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
		   return "redirect:/memLoginForm.do";
		}else {
		   rAttr.addFlashAttribute("msgType","실패 메세지");
		   rAttr.addFlashAttribute("msg","이미 존재하는 회원입니다.");
		   return "redirect:/memJoinForm.do";
		}
	}
	
	@RequestMapping("/memLoginForm.do")
	public String memLoginForm() {
		return "member/memLoginForm";
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
		   m.getMemAge()==0         || m.getAuthList().size()==0
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
		//비밀번호 암호화
		String encyptPw = pwEncoder.encode(m.getMemPassword());
		m.setMemPassword(encyptPw);
		
		int result = membermapper.memUpdate(m);
		if(result == 1) {
		   //기존권한을 삭제하고
			membermapper.authDelete(m.getMemID());

		   //새로운권한 추가하기
		   List<AuthVO> list = m.getAuthList();
		   for(AuthVO authVO : list) {
			   if(authVO.getAuth()!=null) {
				   AuthVO saveVO = new AuthVO();
				   saveVO.setMemID(m.getMemID());
				   saveVO.setAuth(authVO.getAuth());
				   membermapper.authInsert(saveVO);
			   }
		   }
		   rAttr.addFlashAttribute("msgType","성공 메세지");
		   rAttr.addFlashAttribute("msg","회원정보수정에 성공했습니다.");
		   
		   //스프링 보안(새로운 세션 등록)
		   //현재 인증정보 가져오기
		   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		   //회원정보 뽑아오기
		   MemberUser userAccount = (MemberUser) authentication.getPrincipal();
		   //createNewAuthentication에서 리턴받은 세션 객체를 이용해서 세션 업데이트
		   SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(authentication,userAccount.getMember().getMemID()));
		   
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
		
		// 스프링보안(새로운 인증 세션을 생성->객체바인딩)
		
		//기존 인증정보(세션) 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//회원정보(MemberUser) 뽑아오기
		MemberUser userAccount = (MemberUser) authentication.getPrincipal();
		//createNewAuthentication에서 리턴받은 세션 객체를 이용해서 세션 업데이트
		SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(authentication,userAccount.getMember().getMemID()));
				
	    rAttr.addFlashAttribute("msgType","성공 메세지");
	    rAttr.addFlashAttribute("msg","이미지변경에 성공했습니다.");
		
		return "redirect:/";
	}
	
	 // 스프링 보안(새로운 세션 생성 메서드)
	 // UsernamePasswordAuthenticationToken -> 회원정보+권한정보
	 protected Authentication createNewAuthentication(Authentication currentAuth, String username) {
		 	//데이터베이스에서 회원에 대한 정보 가져오기
		    UserDetails newPrincipal = memberUserDetailsService.loadUserByUsername(username);
		    
		    //UsernamePasswordAuthenticationToken로 새로운 세션 객체 만들기 
		    //(newPrincipal : 새로운 회원정보, currentAuth : 현재있는 회원정보, newPrincipal.getAuthorities() : 새로운 권한 정보)
		    UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), newPrincipal.getAuthorities());
		    
		    //setDetails 메서드에 새로운 인증정보를 저장
		    newAuth.setDetails(currentAuth.getDetails());	    
		    return newAuth;
	 }
	
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		return "access-denied";
	}
	
}//class













