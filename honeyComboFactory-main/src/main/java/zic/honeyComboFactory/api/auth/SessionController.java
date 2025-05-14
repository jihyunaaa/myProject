package zic.honeyComboFactory.api.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class SessionController { // 세션 관련 기능

	// 세션에 저장된 회원 정보 반환 기능
	@GetMapping("/api/session/member")
	public Map<String, Object> getLoginedMemberInfo(HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		
		// 세션에서 번호 가져오기
		Object memberNumber = session.getAttribute("loginedMemberNumber");
		// 세션에서 관리자 여부 가져오기
		Object isAdmin = session.getAttribute("memberIsAdmin");

		// 로그인 중이라면
		if (memberNumber == null) {
			result.put("authenticated", false);
		} 
		// 비 로그인중이라면
		else {
			result.put("authenticated", true);
			result.put("loginedMemberNumber", memberNumber);
			result.put("memberIsAdmin", isAdmin);
		}
		
		return result;
	}
	
}
