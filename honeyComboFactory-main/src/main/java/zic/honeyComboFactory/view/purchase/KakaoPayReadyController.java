package zic.honeyComboFactory.view.purchase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.productComboVO.ProductComboService;
import zic.honeyComboFactory.biz.productComboVO.ProductComboVO;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleService;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;
import zic.honeyComboFactory.common.service.ProductService;
import zic.honeyComboFactory.common.service.PurchaseTransactionMybatisService;

@Controller
public class KakaoPayReadyController { // 카카오페이 준비 처리
	@Autowired
	ProductService productService;
	@Autowired
	PurchaseTransactionMybatisService purchaseTransactionMybatisService;
	@Autowired
	private ProductComboService productComboService;
	@Autowired
	private ProductSingleService productSingleService;

	@PostMapping("/purchase/kakao/ready") // POST 요청일 때
	@ResponseBody
	public ResponseEntity<?> kakaoPayReady(@RequestParam String cartProductNumberDatas, HttpSession session,
			ProductComboVO productComboVO, ProductSingleVO productSingleVO) {
		// 카카오페이 결제용 시크릿 키
		final String SECRET_KEY = "SECRET_KEY api키";
		// 카카오페이 결제 준비 URL
		final String KAKAO_PAY_READY_URL = "https://open-api.kakaopay.com/online/v1/payment/ready";
		// 결제 준비 - 승인 URL
		final String approval_url = "http://localhost:8088/kakaoPayApproval.do";
		// 결제 준비 - 취소 URL
		final String cancel_url = "http://localhost:8088/kakaoPayCancel.do";
		// 결제 준비 - 실패 URL
		final String fail_url = "http://localhost:8088/kakaoPayFail.do";

		System.out.println("카카오 결제 준비용 상품 번호 : [" + cartProductNumberDatas + "]");

		// 재고 검사
		if (!this.productService.checkStock(cartProductNumberDatas, session)) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "결제 요청 실패", "message", "재고 부족"));
		}

		try {
			// "+"로 연결된 상품 번호 분리
			String[] productNumbers = cartProductNumberDatas.split("\\+");

			for (String productNumber : productNumbers) {
				System.out.println("+분리한 상품 번호 : [" + productNumber + "]");
			}

			// 1. productNumber로 바로 찾을 수 있는 Map 만들기
			// => 장바구니에서 키값이 productNumber인 정보만 빼서 Map 새로 생성
			ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session
					.getAttribute("shoppingCart");

			Map<String, Map<String, Object>> cartMap = new HashMap<String, Map<String, Object>>();
			for (Map<String, Object> item : shoppingCart) {
				cartMap.put(item.get("productNumber").toString(), item);
			}

			// 2. 새로 생성한 Map에 반복해서 바로 찾기
			// 주문 정보 저장할 배열 orderList 선언
			List<Map<String, Object>> orderList = new ArrayList<>();
			int totalAmount = 0;
			int cart_cnt = 1;
			String product_name = "";

			for (String number : productNumbers) {
				Map<String, Object> item = cartMap.get(number);
				if (item != null) { // 혹시라도 장바구니에 없는 상품번호를 대비
					int count = (int) item.get("cartProductCount");

					// DB를 건드려서 가격 받아오기
					int price = 0;
					if (Boolean.TRUE.equals(item.get("isComboProduct"))) { // 꿀조합 상품이라면
						productComboVO.setCondition("SELECTONECOMBODISCOUNTEDPRICEANDCOMBONAME");
						productComboVO.setProductComboNumber(Long.parseLong(number));

						productComboVO = this.productComboService.getOne(productComboVO);
						price = productComboVO.getProductComboDiscountedPrice();
						product_name = productComboVO.getProductComboName();
					} else { // 개별 상품이라면
						productSingleVO.setCondition("SELECTONESINGLEDISCOUNTEDPRICEANDSINGLENAME");
						productSingleVO.setProductSingleNumber(Long.parseLong(number));

						productSingleVO = this.productSingleService.getOne(productSingleVO);
						price = productSingleVO.getProductSingleDiscountedPrice();
						product_name = productSingleVO.getProductSingleName();
					}

					System.out.println("금액 총합 누적할 상품 가격 : ["+price+"]");
					totalAmount += count * price;
					cart_cnt = count;

					// 주문 정보 저장
					Map<String, Object> orderItem = new HashMap<>();
					orderItem.put("productNumber", number);
					orderItem.put("isComboProduct", item.get("isComboProduct"));
					orderItem.put("count", count);
					orderList.add(orderItem);
				}
			}
			System.out.println("계산된 총 구매 가격 : [" + totalAmount + "]");
			System.out.println("계산된 총 구매 개수 : [" + cart_cnt + "]");
			System.out.println("상품 이름 하나 : [" + product_name + "]");
			System.out.println("저장된 주문 정보 : [" + orderList + "]");

			// 구매 상품 종류 개수
			int totalCount = productNumbers.length;
			System.out.println("구매 상품 종류 개수 : [" + totalCount + "]");

			// 필요 정보 : 회원번호, 상품번호("+"로 연결), 상품이름, 상품가격(=총 가격 적용), 상품수량
			if (1 < totalCount) { // 2개 이상 구매라면
				product_name += " 외 " + (totalCount - 1) + "개 상품";
				cart_cnt = 1;
			}

			String memberNumber = session.getAttribute("loginedMemberNumber").toString();
			String product_num = cartProductNumberDatas;
			int product_price = totalAmount;

			System.out.println("결제 준비할 회원 번호 : [" + memberNumber + "]");
			System.out.println("결제 준비할 상품 번호 : [" + product_num + "]");
			System.out.println("결제 준비할 상품 이름 : [" + product_name + "]");
			System.out.println("결제 준비할 상품 가격 : [" + product_price + "]");
			System.out.println("결제 준비할 상품 수량 : [" + cart_cnt + "]");

			// 주문 번호 생성
			String partner_order_id = String.valueOf(this.purchaseTransactionMybatisService.generatePurchaseNumber());
			System.out.println("결제 준비할 주문 번호 : [" + partner_order_id + "]");

			// 카카오페이 결제 준비 요청 URL
			URL url = new URL(KAKAO_PAY_READY_URL);

			// HTTP 연결 설정
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST"); // POST 요청 방식
			conn.setRequestProperty("Authorization", SECRET_KEY); // API 키 설정
			conn.setRequestProperty("Content-Type", "application/json"); // 요청 데이터 타입
			conn.setDoOutput(true);
			System.out.println("HTTP 연결 설정 완료");

			// 요청 데이터 생성
			Map<String, Object> approvalData = new HashMap<>();
			approvalData.put("cid", "TC0ONETIME"); // 테스트 가맹점 코드
			approvalData.put("partner_order_id", partner_order_id);
			approvalData.put("partner_user_id", memberNumber);
			approvalData.put("item_name", product_name);
			approvalData.put("quantity", cart_cnt);
			approvalData.put("total_amount", product_price);
			approvalData.put("item_code", product_num);
			approvalData.put("tax_free_amount", 0);
			approvalData.put("approval_url", approval_url);
			approvalData.put("cancel_url", cancel_url);
			approvalData.put("fail_url", fail_url);
			System.out.println("결제준비 요청할 데이터 : [" + approvalData + "]");

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonPayload = objectMapper.writeValueAsString(approvalData);

			// 요청 데이터 전송
			try (OutputStream os = conn.getOutputStream()) {
				os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
				os.flush();
			}
			System.out.println("요청 데이터 전송 완료");

			// 응답 코드 확인
			int respCode = conn.getResponseCode();
			if (respCode >= 400) {
				return ResponseEntity.status(respCode).body(Map.of("error", "HTTP 오류 코드: " + respCode));
			}
			System.out.println("응답 코드 확인 완료");

			// 응답 데이터 읽기
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder responseDataBuilder = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				responseDataBuilder.append(line);
			}
			br.close();
			System.out.println("응답 데이터 읽기 완료");

			// 결제 요청 성공 시 tid(거래 ID)와 주문번호, 주문 정보 배열을 Map에 포장해 세션에 저장
			// 결제가 끝날 때까지 계속 들고 다녀야하기 때문
			Map<String, Object> jsonResponse = objectMapper.readValue(responseDataBuilder.toString(), Map.class);
			Map<String, Object> orderInfo = new HashMap<>();
			orderInfo.put("tid", jsonResponse.get("tid"));
			orderInfo.put("partner_order_id", partner_order_id);
			orderInfo.put("orderList", orderList);
			orderInfo.put("totalAmount", product_price);
			orderInfo.put("cartProductNumberDatas", cartProductNumberDatas);

			session.setAttribute("orderInfo", orderInfo);

			// 클라이언트로 JSON 응답 전송
			return ResponseEntity.ok(jsonResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "결제 요청 실패", "message", e.getMessage()));
		}
	}
}
