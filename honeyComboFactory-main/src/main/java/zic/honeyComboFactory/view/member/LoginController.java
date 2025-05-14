package zic.honeyComboFactory.view.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.memberVO.MemberService;
import zic.honeyComboFactory.biz.memberVO.MemberVO;
import zic.honeyComboFactory.common.util.PasswordEncoderUtil;

@Controller
public class LoginController { // 로그인 처리
	@Autowired
	private MemberService memberService;

	// 로그인 기능
	@PostMapping("/member/login")
	@ResponseBody
	public Map<String, Object> login(PasswordEncoderUtil passwordEncoderUtil, MemberVO memberVO, HttpSession session) {
		System.out.println("로그인 컨트롤러 진입");
		System.out.println("로그인 할 회원 아이디 [" + memberVO.getMemberId() + "]");
		System.out.println("로그인 할 회원 비밀번호 [" + memberVO.getMemberPassword() + "]");

		// 소셜 로그인 회원 최소 시작 번호
		final long socialLoginMemberStartNumber = 20000;
		// 반환할 객체
		Map<String, Object> result = new HashMap<>();
		result.put("member", null);

		// 로그인 불가 상태 설정
		if (memberVO.getMemberId() == null && memberVO.getMemberPassword() == null) {
			return result;
		}

		// 인코딩 비밀번호 검사를 위해 변수화
		String rawPassword = memberVO.getMemberPassword();
		System.out.println("인코딩을 위해 변수화한 입력받은 비밀번호 : [" + rawPassword + "]");

		memberVO.setCondition("SELECTONELOGIN");
		// DB 연동
		memberVO = this.memberService.getOne(memberVO);

		System.out.println("로그인-DB에게 받은 회원 정보 : [" + memberVO + "]");
		// 계정이 존재하지 않는다면
		if (memberVO == null) {
			System.out.println("존재하지 않는 계정");
			return result;
		}

		// 탈퇴한 회원이라면
		if (memberVO.isMemberIsWithdraw()) {
			System.out.println("탈퇴한 회원");
			return result;
		}

		// 간편 로그인 계정이라면
		if (socialLoginMemberStartNumber <= memberVO.getMemberNumber()) {
			System.out.println("간편 로그인 계정");
			return result;
		}

//		System.out.println("----------------------------------------");
//		System.out.println("암호화 비밀번호 비교");
//		System.out.println("입력받은 비밀번호 : "+rawPassword+"]");
//		System.out.println("암호화된 비밀번호 : "+memberVO.getMemberPassword()+"]");
//		System.out.println("----------------------------------------");
//		// 암호화된 비밀번호 비교
//		if (!passwordEncoderUtil.matchesPassword(rawPassword, memberVO.getMemberPassword())) {
//			// 비밀번호가 일치하지 않으면
//			System.out.println("비밀번호 불일치");
//			return result;
//		}

		System.out.println("로그인 성공");
		session.setAttribute("memberIsAdmin", memberVO.isMemberIsAdmin());
		session.setAttribute("loginedMemberNumber", memberVO.getMemberNumber());

		// 로그인 성공 시 member null 아닌 값 세팅
		result.put("member", memberVO);
		return result;
	}

	// 로그아웃 기능
	@PostMapping("/member/logout")
	@ResponseBody
	public String logout(HttpSession session) {
		// 회원 번호/관리자 여부/장바구니 세션 삭제
		session.removeAttribute("loginedMemberNumber");
		session.removeAttribute("memberIsAdmin");
		session.removeAttribute("shoppingCart");
		System.out.println("로그아웃 성공");
		return "true";
	}

	// 재로그인(로그아웃) 기능
	@GetMapping("/logout.did")
	public String reLogin(HttpSession session) {
		// 회원 번호/관리자 여부/장바구니 세션 삭제
		session.removeAttribute("loginedMemberNumber");
		session.removeAttribute("memberIsAdmin");
		session.removeAttribute("shoppingCart");
		System.out.println("로그아웃 성공");
		return "redirect:login.do";
	}
}
