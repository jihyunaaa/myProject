package zic.honeyComboFactory.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component // 의존성 주입(DI)을 위해 선언
public class PasswordEncoderUtil {

	// BCrypt 해시 알고리즘을 사용하는 인코더 인스턴스
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	// 받은 비밀번호를 BCrypt 방식으로 암호화
	public String encryptPassword(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	// 입력된 비밀번호와 암호화된 비밀번호를 비교 후 일치 여부 확인
	public boolean matchesPassword(String rawPassword, String encodedPassword) {
		return encoder.matches(rawPassword, encodedPassword);
	}
}