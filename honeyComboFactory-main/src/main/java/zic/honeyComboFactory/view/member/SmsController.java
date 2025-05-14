package zic.honeyComboFactory.view.member;

import java.util.Map;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.external.sms.SmsAPIService;

@Controller
public class SmsController {

	@Autowired
	private SmsAPIService smsAPIService;
	
	@PostMapping("/member/sendSmsCode") // HTTP POST 방식으로 "/sendSmsCode" URL을 처리
	@ResponseBody
	public ResponseEntity<?> sendSmsCode(
			@RequestParam() String phoneNumber, HttpSession session) { // 세션 객체로 인증번호 저장용
		String authcode = smsAPIService.sendSmsCode(phoneNumber);  // 서비스에서 인증번호 생성 + 문자 전송
		session.setAttribute("authCode", authcode);
		System.out.println("[컨트롤러] 인증번호 전송: " + authcode);
		// 응답 본문에 "success" 문자열 전달 (HTTP 200 OK)
		return ResponseEntity.ok(Map.of("status","success"));
	}

	@PostMapping("/member/checkSmsCode")// HTTP POST 방식으로 "/checkSmsCode" URL 요청 처리
	@ResponseBody
	public ResponseEntity<?> checkSmsCode(
			@RequestParam("authCode") String inputCode, HttpSession session) { // 저장된 인증번호 가져오기 위한 세션
		 // 세션에서 이전에 저장된 인증번호 가져옴
		String savedCode = (String) session.getAttribute("authCode");
		   // 입력된 인증번호와 세션 값 비교 (일치 여부 확인)
		boolean isValid = inputCode != null && inputCode.equals(savedCode);
		System.out.println("[컨트롤러] 인증번호 검증: 입력값=" + inputCode + ", 세션값=" + savedCode + ", 일치=" + isValid);
		  // 인증 성공 시, 인증 완료 플래그 저장
	    if (isValid) {
	        session.setAttribute("isSmsVerified", true);
	    }
		
		// JSON 형식으로 인증 결과 응답
		return ResponseEntity.ok(Map.of("valid", isValid));
	}

}
