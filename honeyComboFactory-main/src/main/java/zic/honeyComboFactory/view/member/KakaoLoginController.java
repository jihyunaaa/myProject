package zic.honeyComboFactory.view.member;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.memberVO.MemberService;
import zic.honeyComboFactory.biz.memberVO.MemberVO;
import zic.honeyComboFactory.common.util.PasswordGeneratorUtil;

@Controller
public class KakaoLoginController { // 카카오 로그인 컨트롤러
	@Autowired
	private MemberService memberService;

	@PostMapping("/member/login/kakao")
	@ResponseBody
	public ResponseEntity<String> kakaoLogin(@RequestBody Map<String, Object> kakaoLoginResponse, MemberVO memberVO,
			HttpSession session) {
		System.out.println("카카오 로그인 컨트롤러 진입");
		System.out.println("받은 카카오로그인 반환 정보 : [" + kakaoLoginResponse + "]");
		try {
			// 'kakao_account' 객체를 추출하여 필요 정보 가져오기
			Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoLoginResponse.get("kakao_account");
			String name = kakaoAccount.get("name").toString();
			// 카카오에게 받은 이메일 주소에서 @ 앞부분만 추출
			String emailId = kakaoAccount.get("email").toString().split("@")[0];
			// 카카오에게 받은 이메일 주소에서 @ 뒤부분만 추출
			String emailDomain = kakaoAccount.get("email").toString().split("@")[1];
			// 카카오에게 받은 핸드폰번호에서 +82는 0으로 변경, "-"는 없애기
			String phoneNumber = kakaoAccount.get("phone_number").toString().replace("+82 ", "0").replaceAll("-", "")
					.trim();
			// 생년월일 하나로 만들기 작업
			// 생년
			String birthyear = kakaoAccount.get("birthyear").toString();
			// 월일 (MMDD 형식)
			String birthday = kakaoAccount.get("birthday").toString();
			// 생년월일 (YYYY-MM-DD 형식)
			String birth = birthyear + "-" + birthday.substring(0, 2) + "-" + birthday.substring(2);
			// 로그 찍기
			System.out.println("카카오 로그인 이름 : [" + name + "]");
			System.out.println("카카오 로그인 이메일아이디 : [" + emailId + "]");
			System.out.println("카카오 로그인 이메일도메인 : [" + emailDomain + "]");
			System.out.println("카카오 로그인 핸드폰번호 : [" + phoneNumber + "]");
			System.out.println("카카오 로그인 생년월일 : [" + birth + "]");

			// 회원 정보 저장
			memberVO.setCondition("SELECTONEMEMBER");
			memberVO.setMemberId(emailId);
			memberVO.setMemberName(name);
			memberVO.setMemberEmailId(emailId);
			memberVO.setMemberEmailDomain(emailDomain);
			memberVO.setMemberPhoneNumber(phoneNumber);
			memberVO.setLoginType(0);
			memberVO.setMemberBirth(java.sql.Date.valueOf(birth));

			// 저장 정보 확인
			MemberVO isJoinedMember = this.memberService.getOne(memberVO);
			System.out.println("카카오 로그인-DB 저장 기록 확인 : [" + isJoinedMember + "]");

			boolean flag = true;
			if (isJoinedMember != null) { // DB에 회원번호가 있다면
				System.out.println("카카오 로그인-저장 기록 있음");
				// 두 객체의 값이 모두 같지 않다면
				if (!(memberVO.equals(isJoinedMember))) {
					System.out.println("두 객체의 값이 하나라도 다름");

					// 랜덤 비밀번호 생성
					String password = PasswordGeneratorUtil.generateRandomPassword();
					System.out.println("카카오 로그인-랜덤 생성된 비밀번호 : [" + password + "]");

					memberVO.setMemberPassword(password); // 생성된 비밀번호 설정

					flag = this.memberService.update(memberVO);
				}
			} else { // DB에 회원번호가 없다면
				System.out.println("카카오 로그인-저장 기록 없음");

				// 랜덤 비밀번호 생성
				String password = PasswordGeneratorUtil.generateRandomPassword();
				System.out.println("카카오 로그인-랜덤 생성된 비밀번호 : [" + password + "]");

				memberVO.setMemberPassword(password); // 생성된 비밀번호 설정
				memberVO.setCondition("INSERTSOCIAL");

				flag = this.memberService.insert(memberVO);
			}

			if (!flag) { // 저장/업뎃에 실패했다면
				System.out.println("카카오 로그인-DB 정보 저장/업뎃 실패");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("false");
			}

			// 로그인 정보 받아오기용 파서 세팅
			memberVO.setCondition("SELECTONEPHONE");
			memberVO.setMemberPhoneNumber(phoneNumber);
			memberVO = this.memberService.getOne(memberVO);

			// 로그인 성공 시의 필요 세션 세팅
			session.setAttribute("loginedMemberNumber", memberVO.getMemberNumber());
			session.setAttribute("memberIsAdmin", false);
			return ResponseEntity.ok(String.valueOf(memberVO.getMemberNumber()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("false");
		}
	}
}
