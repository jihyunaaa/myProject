package zic.honeyComboFactory.view.member;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class NaverLoginController {
	// 네이버 로그인 요청하는 컨트롤러 
	// 단지 네이버 로그인 화면으로 이동 시키는 역할(URL)
	@RequestMapping("/member/naver/login")
	public void naverLogin(HttpServletResponse response, HttpSession session) throws IOException {
		// 네이버 애플리케이션 설정
		final String clientId = "클라이언트 ID"; // 발급받은 클라이언트 ID
		final String redirectURI = URLEncoder.encode("http://localhost:8088/member/naver/callback", "UTF-8");// 네이버 로그인 후 콜백 URL
		// 새로운 경로 맞춰서 수정
        final String scope = "name,email,birthday,birthyear,mobile"; // 사용자의 정보 동의 받을 값 설정
		
        // 네이버 인증코드
        SecureRandom random = new SecureRandom(); // 인증코드 문자열로 랜덤 생성
        String state = new BigInteger(130, random).toString();
        session.setAttribute("state", state); // 세션에 저장하여 콜백 시 확인
        
        // 로그인 인증 요청 URL
        String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectURI
                + "&state=" + state
                + "&scope=" + scope;

        // 네이버 로그인으로 이동
        response.sendRedirect(naverLoginUrl); 
	}
}

