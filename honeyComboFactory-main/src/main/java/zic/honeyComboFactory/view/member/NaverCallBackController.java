package zic.honeyComboFactory.view.member;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.member.impl.MemberServiceImpl;
import zic.honeyComboFactory.biz.memberVO.MemberVO;
import zic.honeyComboFactory.common.http.HttpClientUtil;
import zic.honeyComboFactory.common.util.PasswordGeneratorUtil;

@Controller
public class NaverCallBackController {
	// 네이버 로그인 실행 후, 토큰과 사용자 정보를 얻고 DB 등록 + 세션에 정보를 담아 메인으로 보내는 네이버 콜백 컨트롤러

	@Autowired
	private MemberServiceImpl memberService; // 회원 정보 조회 및 저장
	@Autowired
	private HttpClientUtil httpClientUtil; // HTTP 요청
	@Autowired
	private ObjectMapper objectMapper; // ObjectMapper를 자동으로 바인딩하지 않기 때문에 DI
	
	public static final int LOGIN_TYPE_NAVER = 1; // 로그인 타입 네이버 상수화

	@GetMapping("/member/naver/callback") // 네이버 로그인 후 콜백
	public ResponseEntity<String> callback(@RequestParam String code, @RequestParam String state, HttpSession session,
			MemberVO memberVO) {
		try {
			// 1. 토큰 요청
			String redirectURI = URLEncoder.encode("http://localhost:8088/member/naver/callback", "UTF-8");
			String tokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code"
					+ "&client_id=클라이언트ID" + "&client_secret=시크릿ID" + "&redirect_uri=" + redirectURI
					+ "&code=" + code + "&state=" + state;

			// 요청 로직 파일 분리
			String tokenJson = httpClientUtil.post(tokenUrl, "", null); // 토큰 값을 POST 요청으로 받아와서
			System.out.println("tokenJson: [" + tokenJson + "]"); // 토근 값 확인
			Map<String, Object> tokenMap = objectMapper.readValue(tokenJson, new TypeReference<>() {
			});
			// 네이버가 응답한 JSON 형태의 데이터를 Map 형태로 파싱한다.

			// 토큰 값 추출
			String accessToken = (String) tokenMap.get("access_token"); // 값을 꺼내니까 get

			if (accessToken == null || accessToken.isEmpty()) { // 만약 토큰이 없으면
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("false"); // 로그인 실패
			}

			// 2. 사용자 정보 요청
			String profileJson = httpClientUtil.getWithAuth("https://openapi.naver.com/v1/nid/me", accessToken);
			// 사용자 정보를 네이버에게 받아오는 코드
			// 사용자 정보를 JSON으로 받아 MAP으로 변환
			Map<String, Object> profileMap = objectMapper.readValue(profileJson, new TypeReference<>() {
			});
			@SuppressWarnings("unchecked")
			Map<String, Object> response = (Map<String, Object>) profileMap.get("response");

			if (response == null) { // 받은 응답이 없다면
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("false"); // 실패
			}

			// 3. 회원 정보 추출
			String email = (String) response.get("email");
			String name = (String) response.get("name");
			String birthYear = (String) response.get("birthyear");
			String birthday = (String) response.get("birthday");
			String mobile = (String) response.get("mobile");

			// 이메일 형식 검증
			if (email == null || !email.contains("@")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("false");
			}

			// 이메일 아이디/도메인 분리 및 DB 조회
			// 이메일 분리
			String emailId = email.split("@")[0];
			String emailDomain = email.split("@")[1];

			// 생년월일은 출생년도와 생일을 받아 하나의 데이터로 변환
			// 파싱해올 때 데이터 저장 형식 바꿈
			// 1. birthday 포맷 먼저 조정
			if (!birthday.contains("-") && birthday.length() == 4) {
				birthday = birthday.substring(0, 2) + "-" + birthday.substring(2, 4);
			}

			// 2. "2000-08-14" 형식으로 변환
			String birth = birthYear + "-" + birthday;

			// 3. // 문자열을 Date 형식으로 변환
			Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birth);

			// 회원 VO 세팅
			memberVO.setMemberEmailId(emailId); // 회원 정보 세팅
			memberVO.setMemberEmailDomain(emailDomain);
			memberVO.setMemberId(emailId); // 소셜 아이디
			memberVO.setMemberName(name);
			memberVO.setMemberPhoneNumber(mobile);
			memberVO.setMemberBirth(new java.sql.Date(birthDate.getTime()));

			System.out.println("조회 조건 - 이메일 ID: [" + memberVO.getMemberEmailId() + "]");
			System.out.println("조회 조건 - 이메일 도메인: [" + memberVO.getMemberEmailDomain() + "]");
			memberVO.setCondition("SELECTONEEMAIL"); // 일치하는 이메일을 가진 회원이 있는지 확인

			System.out.println("ID: [" + memberVO.getMemberEmailId() + "]");
			System.out.println("DOMAIN: [" + memberVO.getMemberEmailDomain() + "]");
			System.out.println("조건: " + memberVO.getCondition());

			MemberVO isJoinedMember = this.memberService.getOne(memberVO);
			System.out.println("isJoinedMember: [" + isJoinedMember + "]");

			boolean flag = true;
			if (isJoinedMember != null && isJoinedMember.getMemberNumber() > 0) { // 실제 회원이 존재하는 경우에만
				System.out.println("기존 회원 발견");
				if (!memberVO.equals(isJoinedMember)) {
					System.out.println("회원 정보 업데이트 시도");
					
					// 랜덤 비밀번호 생성
					String password = PasswordGeneratorUtil.generateRandomPassword();
					System.out.println("카카오 로그인-랜덤 생성된 비밀번호 : [" + password + "]");
					
					memberVO.setMemberPassword(password); // 생성된 비밀번호 설정
					
					flag = this.memberService.update(memberVO);
				}
			} else { // null이거나 빈 객체인 경우
				System.out.println("신규 회원 가입 시도");

				// 랜덤 비밀번호 생성
				String password = PasswordGeneratorUtil.generateRandomPassword();
				System.out.println("카카오 로그인-랜덤 생성된 비밀번호 : [" + password + "]");
				
				memberVO.setMemberPassword(password); // 생성된 비밀번호 설정
				memberVO.setLoginType(1); // 로그인 타입을 네이버로 설정
				memberVO.setCondition("INSERTSOCIAL");
				
				flag = this.memberService.insert(memberVO);

				System.out.println("가입 결과: [" + flag + "]");
			}
			if (!flag) { // DB 저장 실패 시
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("false"); // 실패 반환
			}

			// 로그인 처리
			memberVO.setCondition("SELECTONELOGIN");
			memberVO.setMemberId(emailId);
			memberVO = this.memberService.getOne(memberVO);

			// 5. 세션에 필요한 정보 저장
			session.setAttribute("loginedMemberNumber", memberVO.getMemberNumber()); // 회원번호
			session.setAttribute("loginedmemberIsAdmin", memberVO.isMemberIsAdmin()); // 관리자 여부

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("false");
		}
		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/main.do").build();
	}

}
