package zic.honeyComboFactory.view.purchase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailVO;
import zic.honeyComboFactory.biz.purchaseVO.PurchaseVO;
import zic.honeyComboFactory.common.service.PurchaseProcessService;

@Controller
public class KakaoPayApprovalController { // 카카오페이 승인 처리
	@Autowired
	private PurchaseProcessService purchaseProcessService;

	@PostMapping("/purchase/kakao/approval") // POST 요청일 때
	@ResponseBody
	public ResponseEntity<?> kakaoPayApproval(@RequestParam String pg_token, HttpSession session, PurchaseVO purchaseVO,
			PurchaseDetailVO purchaseDetailVO) {
		// 카카오페이 결제용 시크릿 키
		final String DEV_SECRET_KEY = "DEV_SECRET_KEY api키";
		// 카카오페이 결제 승인 URL
		final String KAKAO_PAY_APPROVAL_URL = "https://open-api.kakaopay.com/online/v1/payment/approve";

		Map<String, Object> orderInfo = (Map<String, Object>) session.getAttribute("orderInfo");
		if (orderInfo == null) { // 결제 준비 정보가 비었다면
			return ResponseEntity.badRequest().body(Map.of("error", "세션 만료 또는 잘못된 요청"));
		}

		// 결제할 상품 번호 목록 변수화
		String cartProductNumberDatas = orderInfo.get("cartProductNumberDatas").toString();
		System.out.println("결제 상품 번호 : [" + cartProductNumberDatas + "]");

		// 문자열을 List<Long>으로 변환 후 오름차순 정렬
		List<Long> sortedCartProductNumberDatas = Arrays.stream(cartProductNumberDatas.split("\\+"))
				.map(Long::parseLong) // Long으로
				.sorted() // 정렬
				.collect(Collectors.toList()); // 리스트로 수집
		System.out.println("정렬된 결제 상품 번호 : [" + sortedCartProductNumberDatas + "]");

		// 결제 승인에 필요한 값 변수화
		String tid = orderInfo.get("tid").toString();
		String partner_order_id = orderInfo.get("partner_order_id").toString();
		String partner_user_id = session.getAttribute("loginedMemberNumber").toString();
		System.out.println("결제 승인할 토큰 : [" + pg_token + "]");
		System.out.println("결제 승인할 식별자 : [" + tid + "]");
		System.out.println("결제 승인할 회원 아이디 : [" + partner_user_id + "]");
		System.out.println("결제 승인할 주문번호 : [" + partner_order_id + "]");

		List<Map<String, Object>> orderList = (List<Map<String, Object>>) orderInfo.get("orderList");
		System.out.println("DB 처리할 결제 승인 상세정보 목록 : [" + orderList + "]");

		long totalAmount = Long.parseLong(orderInfo.get("totalAmount").toString());

		int status = this.purchaseProcessService.processPurchase(session, orderList, totalAmount, purchaseVO, tid,
				partner_order_id, partner_user_id, sortedCartProductNumberDatas, cartProductNumberDatas);

		// 결제 관련 세션 정보 제거
		session.removeAttribute("orderInfo");

		// 결제 처리 실패 시
		if (status == 1) {
			return ResponseEntity.ok(Map.of("status", "fail", "message", "결제 처리 실패"));
		}
		// 재고 부족 시
		else if (status == 2) {
			return ResponseEntity.ok(Map.of("status", "fail", "message", "재고 부족"));
		}

		try {
			// 승인 요청에 필요한 데이터(JSON) 생성
			Map<String, String> approvalData = new HashMap<>();
			approvalData.put("cid", "TC0ONETIME"); // 테스트용 가맹점 코드
			approvalData.put("tid", tid); // 결제 고유 번호
			approvalData.put("partner_order_id", partner_order_id); // 주문번호
			approvalData.put("partner_user_id", partner_user_id); // 회원 아이디(번호)
			approvalData.put("pg_token", pg_token); // 승인 토큰

			// ObjectMapper로 JSON 직렬화
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonData = objectMapper.writeValueAsString(approvalData);

			// 카카오페이 서버와 통신
			URL url = new URL(KAKAO_PAY_APPROVAL_URL);
			// HTTP 연결 설정
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", DEV_SECRET_KEY);
			conn.setRequestProperty("Content-type", "application/json");
			conn.setDoOutput(true);
			System.out.println("HTTP 연결 설정 완료");

			// 데이터를 카카오페이 서버로 전송
			try (OutputStream os = conn.getOutputStream()) {
				os.write(jsonData.getBytes());
			}
			System.out.println("데이터를 카카오페이 서버로 전송 완료");

			// 카카오페이 서버의 응답 받기
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder responseBuilder = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				responseBuilder.append(line);
			}
			br.close();
			System.out.println("카카오페이 서버의 응답 받기 완료");

			// 응답 데이터를 파싱
			Map<String, Object> jsonResponse = objectMapper.readValue(responseBuilder.toString(),
					new TypeReference<>() {
					});

			// 결제 승인 결과를 클라이언트로 반환
			return ResponseEntity.ok(jsonResponse);
		} catch (Exception e) {
			e.printStackTrace();
			// 결제 관련 세션 정보 제거
			session.removeAttribute("orderInfo");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "결제 승인 중 오류 발생"));
		}
	}
}
