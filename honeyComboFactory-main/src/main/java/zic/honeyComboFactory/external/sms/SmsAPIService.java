package zic.honeyComboFactory.external.sms;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Service
public class SmsAPIService {

	private static final DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(
			"api값",//키값
			"api값",//키값
			"https://api.coolsms.co.kr");

	   //인증번호 난수 생성
	private static final SecureRandom secureRandom = new SecureRandom();

	public String sendSmsCode(String toPhoneNumber) { //인증번호 생성해 sms로 전송하는 메서드
		String verificationCode = String.valueOf(100000 + secureRandom.nextInt(900000));

		Message message = new Message();
		message.setFrom("발신번호");
		message.setTo(toPhoneNumber);
		message.setText("[SMS 인증] 인증번호: " + verificationCode + " 를 입력해주세요.");
		System.out.println("[SMS] " + toPhoneNumber + " 로 인증번호 전송됨: " + verificationCode);
         //CoolSMS API를 통해 실제 메시지를 전송
		messageService.sendOne(new SingleMessageSendingRequest(message));
		return verificationCode;
	}
}
