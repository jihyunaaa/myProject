package zic.honeyComboFactory.view.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.memberVO.MemberService;
import zic.honeyComboFactory.biz.memberVO.MemberVO;

@Controller
public class JoinController { // 회원가입 처리
	@Autowired
	private MemberService memberService;

	// 아이디 중복 검사 기능
	@PostMapping("/member/join/checkId")
	@ResponseBody
	public boolean checkJoinId(MemberVO membervo, HttpSession session) {
		System.out.println("아이디 중복검사 컨트롤러 진입");
		System.out.println("가입할 회원 아이디 [" + membervo.getMemberId() + "]");

		// 입력값이 없을 경우 예외 처리
		boolean isAvailable = false;
		if (membervo.getMemberId() != null && !membervo.getMemberId().trim().isEmpty()) {
			membervo.setCondition("SELECTONEMEMBER");

			membervo = this.memberService.getOne(membervo);

			System.out.println("입력한 아이디 존재하면 정보 [" + membervo + "]");

			if (membervo == null) {
				isAvailable = true;
			}
		}
		System.out.println("중복 여부 [" + isAvailable + "]");
		return isAvailable;
	}

	// 회원가입 기능
	@PostMapping("/member/join")
	@ResponseBody
	public String join(MemberVO membervo, HttpSession session) {
		System.out.println("회원가입 컨트롤러 진입");
		System.out.println("회원 아이디 [" + membervo.getMemberId() + "]");
		System.out.println("회원 아이디 [" + membervo.getMemberPassword() + "]");
		System.out.println("회원 아이디 [" + membervo.getMemberName() + "]");
		System.out.println("회원 아이디 [" + membervo.getMemberBirth() + "]");
		System.out.println("회원 아이디 [" + membervo.getMemberPhoneNumber() + "]");
		System.out.println("회원 아이디 [" + membervo.getMemberAddressMain() + "]");
		System.out.println("회원 아이디 [" + membervo.getMemberAddressDetail() + "]");
		System.out.println("회원 아이디 [" + membervo.getMemberId() + "]");
		System.out.println("회원 아이디 [" + membervo.getMemberEmailDomain() + "]");

		membervo.setCondition("INSERTJOIN");
		boolean isJoined = this.memberService.insert(membervo);
		System.out.println("회원가입 성공 여부 [" + isJoined + "]");

		// 결과 응답
		if (isJoined) {
			return "true"; // 회원가입 성공 시 "true" 반환
		} else {
			return "false"; // 회원가입 실패 시 "false" 반환
		}
	}
}
