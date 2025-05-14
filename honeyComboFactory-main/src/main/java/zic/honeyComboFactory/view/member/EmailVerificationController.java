package zic.honeyComboFactory.view.member;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.memberVO.MemberService;
import zic.honeyComboFactory.biz.memberVO.MemberVO;

@Controller
@ResponseBody 
public class EmailVerificationController {
	// 인증번호 생성 및 사용자 정보 인증 + 인증번호 일치 여부 확인 기능
	@Autowired
	private MemberService memberService;

	@PostMapping("/member/verifyEmailCode") 
	public Map<String, Object> sendVerificationCode(MemberVO memberVO, HttpSession session) {

		Map<String, Object> result = new HashMap<>(); // 응답 메시지 담을 변수 선언

		try { // 정보 세팅
			memberVO.setCondition("SELECTONEFINDID");
			// 전달받은 회원정보로 DB 조회
			MemberVO member = this.memberService.getOne(memberVO);

			// 조회 실패 시 처리
			if(member == null) {
				 result.put("status", "fail");
			        result.put("message", "일치하는 회원 정보가 없습니다.");
			        return result;
			    }
			// 인증번호 6자리 생성
			String verificationCode = String.format("%06d", new Random().nextInt(1000000));
			// 세션에 인증번호와 조회된 회원 ID를 저장함
			session.setAttribute("verificationCode", verificationCode);

			MemberVO sessionId = this.memberService.getOne(memberVO); 
			session.setAttribute("findId", sessionId.getMemberId()); 
			
			// 이메일 전송 설정
			String to = memberVO.getMemberEmailId() + "@" + memberVO.getMemberEmailDomain(); // 받는 사람
			String from = "이메일"; // 보내는 사람
			// SMTP 서버 설정
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com"); 
			props.put("mail.smtp.port", "587"); 
			props.put("mail.smtp.starttls.enable", "true"); // STARTTLS 활성화
	        props.put("mail.smtp.auth", "true"); // SMTP 인증 활성화
	        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // TLS 버전 맞춤
			
			// Gmail 서버에 로그인하기 위한 인증 세션
			// 사용자의 아이디와 인증번호를 저장하는 세션과 구분됨
			javax.mail.Session mailSession = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("이메일", "앱비밀번호");
				} // 메일 보내기 전에 내 계정 정보도 함께 저장함 (인증)
			});
			// 메시지 생성
			MimeMessage message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(from)); // 보내는 사람
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // 받는 사람
			message.setSubject("[꿀조합팩토리] 인증번호 안내"); // 제목
			message.setText("인증번호는 [" + verificationCode + "] 입니다."); // 내용
			// 이메일 전송
			Transport.send(message);
			// 응답 결과 
			result.put("message", "인증번호가 이메일로 전송되었습니다.");
			result.put("code", verificationCode); 
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message", "서버 오류 또는 이메일 전송 실패");
		}
		return result; // 결과 반환
	}
	
	// 이메일 인증번호 비교 로직
	@PostMapping("/member/checkEmailCode")
	@ResponseBody
	public Map<String, Object> checkEmailCode(@RequestParam("code") String inputCode, HttpSession session) {
	    Map<String, Object> result = new HashMap<>(); // Map 객체 선언
	    // 세션에 저장된 인증번호 가져옴
	    String storedCode = (String) session.getAttribute("verificationCode"); 
	    // 만약 인증번호가 존재하고 사용자가 입력한 인증번호와 같다면
	    if (storedCode != null && storedCode.equals(inputCode)) {
	      // 인증 성공 응답  
	    	result.put("valid", true);
	        // 인증 완료 후 세션에서 인증번호 제거
	        session.removeAttribute("verificationCode");
	    } else { // 인증 실패 시
	        result.put("valid", false); // 인증 실패 응답
	    }

	    return result; // 결과는 JSON으로 반환
	}
}

