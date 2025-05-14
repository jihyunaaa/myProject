package zic.honeyComboFactory.common.util;

import java.security.SecureRandom;

// 단순한 비밀번호 랜덤 생성이라 @Component 불필요
public class PasswordGeneratorUtil {
	
	private static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String lower = "abcdefghijklmnopqrstuvwxyz";
	private static final String digits = "0123456789";
	private static final String specialChars = "!@#$%^&*()-_=+[]{}|;:,.<>?";
	private static final String combinedChars = upper + lower + digits + specialChars;

	private static final SecureRandom random = new SecureRandom();

	public static String generateRandomPassword() {
		StringBuilder password = new StringBuilder();

		// 최소 한 개의 대문자, 소문자, 숫자, 특수문자를 포함하기 위해 랜덤으로 선택
		password.append(upper.charAt(random.nextInt(upper.length())));
		password.append(lower.charAt(random.nextInt(lower.length())));
		password.append(digits.charAt(random.nextInt(digits.length())));
		password.append(specialChars.charAt(random.nextInt(specialChars.length())));

		// 나머지 두 자리는 혼합된 문자로 채워서 총 6자리 비밀번호 생성
		for (int i = 4; i < 6; i++) {
			password.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
		}

		// 랜덤하게 섞기
		String result = password.toString();
		StringBuilder shuffledPassword = new StringBuilder(result);
		for (int i = 0; i < result.length(); i++) {
			int j = random.nextInt(result.length());
			char temp = shuffledPassword.charAt(i);
			shuffledPassword.setCharAt(i, shuffledPassword.charAt(j));
			shuffledPassword.setCharAt(j, temp);
		}

		return shuffledPassword.toString();
	}
}