package zic.honeyComboFactory.common.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Component;
// GET/POST 요청을 처리하는 HTTP Util 

@Component
public class HttpClientUtil { // HTTP 요청을 보내는 클래스
	// 일반 GET 요청					
	public String get(String urlStr) throws Exception {
		URL url = new URL(urlStr); // 요청할 URL 객체 변환
		HttpURLConnection con = (HttpURLConnection) url.openConnection(); // 연결
		con.setRequestMethod("GET"); // GET으로 전달

		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString(); // 결과 문자열 반환
		}
	}
	// 인증 필요한 GET 요청(접근 시 인증 필요)
	public String getWithAuth(String urlStr, String accessToken) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer " + accessToken);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line); // 한 줄씩 읽음
			}
			return sb.toString(); // 전체 결과 반환
		}
	}
	 // POST 요청 
    public String post(String urlStr, String body, Map<String, String> headers) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true); // POST 전송 가능하게 설정
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // 추가 헤더 세팅
        if (headers != null) {
            for (String key : headers.keySet()) {
                con.setRequestProperty(key, headers.get(key));
            }
        }

        // POST 바디 작성
        try (OutputStream os = con.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
            os.flush();
        }

        // 버퍼 리더 (응답 읽어오기)
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }
	
	
}
