package zic.honeyComboFactory.view.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.memberVO.MemberService;
import zic.honeyComboFactory.biz.memberVO.MemberVO;
import zic.honeyComboFactory.common.util.PasswordEncoderUtil;

@Controller
public class PasswordController {
	@Autowired
	private MemberService memberService;

	// 비밀번호 변경 기능
	@PostMapping("/member/password/update")
	@ResponseBody
	public String updatePassword(MemberVO memberVO, HttpSession session) {
		long memberNumber = 0;
		String memberId = null;
		if (session.getAttribute("loginedMemberNumber") != null && memberVO.getMemberId() == null) {
			memberNumber = (long) session.getAttribute("loginedMemberNumber");
		} else if (memberVO.getMemberId() != null && session.getAttribute("loginedMemberNumber") == null) {
			memberId = memberVO.getMemberId();
		}
		String newPassword = memberVO.getMemberPassword();

		System.out.println("비밀번호 변경할 회원 번호 [" + memberNumber + "]");
		System.out.println("비밀번호 변경할 회원 아이디 [" + memberId + "]");
		System.out.println("변경할 비밀번호 [" + newPassword + "]");

		memberVO.setCondition("UPDATEPASSWORD");
		memberVO.setMemberPassword(newPassword);

		if (memberNumber != 0 && (memberId == null || memberId.isEmpty())) {
			memberVO.setMemberNumber(memberNumber);
		}

		// 응답 처리
		boolean flag = this.memberService.update(memberVO);
		if (flag) {
			System.out.println("비밀번호 변경 성공");
			return "true";
		} else {
			System.out.println("비밀번호 변경 실패");
			return "false";
		}
	}

	// 본인인증 기능
	@PostMapping("/member/password/confirm")
	@ResponseBody
	public String confirmPassword(@RequestParam("loginedMemberPassword") String memberPassword,
			PasswordEncoderUtil passwordEncoderUtil, MemberVO memberVO, HttpSession session) {
		System.out.println("비밀번호 인증 서블릿 도착");
		long memberNumber = (long) session.getAttribute("loginedMemberNumber");

		System.out.println("본인인증할 회원 번호 [" + memberNumber + "]");
		System.out.println("인증 요청한 비밀번호 [" + memberPassword + "]");

		memberVO.setCondition("SELECTONEMYPAGE");
		memberVO.setMemberNumber(memberNumber);
		memberVO.setMemberPassword(memberPassword);
		memberVO = this.memberService.getOne(memberVO);
		System.out.println("인증 요청한 회원 정보 [" + memberVO + "]");

		System.out.println("----------------------------------------");
		System.out.println("암호화 비밀번호 비교");
		System.out.println("입력받은 비밀번호 : " + memberPassword + "]");
		System.out.println("암호화된 비밀번호 : " + memberVO.getMemberPassword() + "]");
		System.out.println("----------------------------------------");
		// 암호화된 비밀번호 비교
		if (!passwordEncoderUtil.matchesPassword(memberPassword, memberVO.getMemberPassword())) {
			// 비밀번호가 일치하지 않으면
			System.out.println("비밀번호 불일치");
			System.out.println("인증 실패");
			return "false";
		} else { // 비밀번호 일치 시
			System.out.println("인증 확인");
			return "true";
		}
	}
}
