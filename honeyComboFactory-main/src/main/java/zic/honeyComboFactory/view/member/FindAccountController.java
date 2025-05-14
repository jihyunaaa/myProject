package zic.honeyComboFactory.view.member;

import java.util.HashMap;
import java.util.Map;

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
public class FindAccountController {

	@Autowired
	private MemberService memberService;

	// 아이디 찾기 컨트롤러
	@PostMapping("/member/findId")
	@ResponseBody // VIEW가 아닌 문자열을 반환하기 위해서 작성
	public Map<String, Object> FindId(@RequestParam String authMethod, @RequestParam String authValue,
			MemberVO memberVO) {
		// 응답 메시지 담을 변수 선언
		Map<String, Object> result = new HashMap<>();

		// 자판기 코드
		// 0:존재, 1:존재하지 않는 계정, 2:탈퇴한 회원, 3:간편 로그인 계정
		int status = 1;
		result.put("status", status);

		// 값 검증
		String memberBirth = memberVO.getMemberBirth().toString();
		if (authValue == null || authValue.trim().isEmpty() || memberBirth == null || memberBirth.trim().isEmpty()) {
			return result;
		}

		// 조건 설정
		memberVO.setCondition("SELECTONEFINDID");

		if ("phoneNum".equals(authMethod)) {
			memberVO.setMemberPhoneNumber(authValue);
		} else if ("email".equals(authMethod)) {
			String[] emailParts = authValue.split("@");
			if (emailParts.length != 2)
				return result;
			memberVO.setMemberEmailId(emailParts[0]);
			memberVO.setMemberEmailDomain(emailParts[1]);
		} else {
			return result;
		}

		// DB 건드리기
		memberVO = memberService.getOne(memberVO);
		System.out.println("아이디 찾기-DB에게 받은 회원 정보 : [");
		System.out.println("번호 : " + memberVO.getMemberNumber());
		System.out.println("탈퇴여부 : " + memberVO.isMemberIsWithdraw());
		System.out.println("아이디 : " + memberVO.getMemberId());
		System.out.println("]");

		// 옳은 계정 상태 확인 함수 호출
		status = checkMember(memberVO);
		// 옳은 계정이라면
		if (status == 0) {
			System.out.println("옳은 계정");
			// 회원 아이디 저장
			result.put("memberId", memberVO.getMemberId());
		}
		// 계정 상태 덮어쓰기
		result.put("status", status);
		
		return result;
	}

	// 비밀번호 찾기 컨트롤러
	@PostMapping("/member/findPassword")
	@ResponseBody // true, false 값을 문자열 그대로 AJAX에게 전달
	public int findPassword(@RequestParam String authMethod, @RequestParam String authValue,
			PasswordEncoderUtil passwordEncoderUtil, MemberVO memberVO) {
		// 자판기 코드
		// 0:존재, 1:존재하지 않는 계정, 2:탈퇴한 회원, 3:간편 로그인 계정
		int status = 1;

		// 생년월일, 인증 입력값이 없을 경우
		String memberBirth = memberVO.getMemberBirth().toString();
		if (memberVO.getMemberId() == null || memberVO.getMemberId().trim().isEmpty() || memberBirth == null
				|| memberBirth.trim().isEmpty() || authValue == null || authValue.trim().isEmpty()) {
			return status;
		}

		// 해싱 비밀번호 검사를 위해 변수화
		String rawPassword = memberVO.getMemberPassword();

		// 조건 설정
		memberVO.setCondition("SELECTONEFINDPASSWORD");

		if ("phoneNum".equals(authMethod)) {
			memberVO.setMemberPhoneNumber(authValue);
		} else if ("email".equals(authMethod)) {
			String[] emailParts = authValue.split("@");
			if (emailParts.length != 2) {
				return status;
			}
			memberVO.setMemberEmailId(emailParts[0]);
			memberVO.setMemberEmailDomain(emailParts[1]);
		} else {
			return status;
		}

		// DB 건드리기
		memberVO = memberService.getOne(memberVO);
		System.out.println("비밀번호 찾기-DB에게 받은 회원 정보 : [");
		System.out.println("번호 : " + memberVO.getMemberNumber());
		System.out.println("탈퇴여부 : " + memberVO.isMemberIsWithdraw());
		System.out.println("비밀번호 : " + memberVO.getMemberPassword());
		System.out.println("]");

		// 옳은 계정 상태 확인 함수 호출
		status = checkMember(memberVO);
		// 옳은 계정이 아니라면
		if (status != 0) {
			System.out.println("옳지 않은 계정");
			return status;
		}

		System.out.println("----------------------------------------");
		System.out.println("암호화 비밀번호 비교");
		System.out.println("입력받은 비밀번호 : " + rawPassword + "]");
		System.out.println("암호화된 비밀번호 : " + memberVO.getMemberPassword() + "]");
		System.out.println("----------------------------------------");
		// 암호화된 비밀번호 비교
		if (!passwordEncoderUtil.matchesPassword(rawPassword, memberVO.getMemberPassword())) {
			// 비밀번호가 일치하지 않으면
			System.out.println("비밀번호 불일치");
			System.out.println("인증 실패");
			status = 1;
		} else { // 비밀번호 일치 시
			System.out.println("인증 확인");
			status = 0;
		}

		return status;
	}

	// 비밀번호 재설정
	@PostMapping("/member/updatePassword")
	@ResponseBody
	public String updatePassword(@RequestParam String newPassword, MemberVO memberVO, HttpSession session) {
		System.out.println("비밀번호 재설정할 회원아이디 : [" + memberVO.getMemberId() + "]");
		System.out.println("비밀번호 재설정할 비밀번호 : [" + newPassword + "]");

		memberVO.setMemberPassword(newPassword); // 새 비밀번호 설정
		memberVO.setCondition("UPDATEPASSWORD");

		boolean updateFlag = memberService.update(memberVO);
		if (updateFlag) { // 재설정 성공 시
			return "true";
		} else { // 재설정 실패 시
			return "false";
		}
	}

	// 옳은 계정 상태 검사 기능
	private int checkMember(MemberVO memberVO) {
		// 소셜 로그인 회원 최소 시작 번호
		final long socialLoginMemberStartNumber = 20000;

		// 계정이 존재하지 않는다면
		if (memberVO == null) {
			System.out.println("존재하지 않는 계정");
			return 1;
		}

		// 탈퇴한 회원이라면
		if (memberVO.isMemberIsWithdraw()) {
			System.out.println("탈퇴한 회원");
			return 2;
		}

		// 간편 로그인 계정이라면
		if (socialLoginMemberStartNumber <= memberVO.getMemberNumber()) {
			System.out.println("간편 로그인 계정");
			return 3;
		}

		return 0;
	}
}