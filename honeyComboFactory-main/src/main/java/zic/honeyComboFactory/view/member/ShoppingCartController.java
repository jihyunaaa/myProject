package zic.honeyComboFactory.view.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShoppingCartController { // 장바구니 컨트롤러

	// 장바구니 상품 담기 기능
	@PostMapping("/member/cart/add")
	@ResponseBody
	public String addShoppingCart(@RequestParam int productNumber, @RequestParam int cartProductCount, @RequestParam boolean isComboProduct, HttpSession session) {
		System.out.println("장바구니 상품 담기 컨트롤러 진입");
		System.out.println("장바구니 담을 상품 번호 : [" + productNumber + "]");
		System.out.println("장바구니 담을 상품 수량 : [" + cartProductCount + "]");
		System.out.println("장바구니 담을 조합상품 여부 : [" + isComboProduct + "]");
		
		ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session
				.getAttribute("shoppingCart");

		// 장바구니 null 검사 및 초기화
		if (shoppingCart == null) {
		    shoppingCart = new ArrayList<>();
		    session.setAttribute("shoppingCart", shoppingCart); // 세션에도 저장
		}
		
		// 이미 들어있는지 여부 = 기본 거짓
		boolean alreadyIn = false;
		for (Map<String, Object> cartItem : shoppingCart) {
			// 받은 상품 번호와 장바구니 안의 상품 번호가 일치하고 조합 상품이라면
			if ((int) cartItem.get("productNumber") == productNumber
					&& (boolean) cartItem.get("isComboProduct") == isComboProduct) {
				// 현재 개수 구하기
				int currentCount = (int) cartItem.get("cartProductCount");
				// 현재 개수 + 추가한 재고
				cartItem.put("cartProductCount", currentCount + cartProductCount);
				// 이미 들어있는지 여부 = 참 으로 변경
				alreadyIn = true;
				break;
			}
		}

		// 만약 담긴 상품이 아니라면
		if (!alreadyIn) {
			Map<String, Object> newCartItem = new HashMap<>();
			// 상품 번호
			newCartItem.put("productNumber", productNumber);
			// 상품 수량
			newCartItem.put("cartProductCount", cartProductCount);
			// 조합 상품 여부
			newCartItem.put("isComboProduct", isComboProduct);
			// 상품에 추가
			shoppingCart.add(newCartItem);
		}

		// 새로운 장바구니 세션에 저장
		session.setAttribute("shoppingCart", shoppingCart);

		return "true";
	}

	// 장바구니 상품 개수 변경 기능
	@PostMapping("/member/cart/change")
	@ResponseBody
	public String changeCartProductCount(@RequestParam int cartProductNumber, @RequestParam String cartProductCondition, @RequestParam int productCount, HttpSession session) {
		System.out.println("장바구니 상품 개수변경 컨트롤러 진입");

		// 장바구니 가져오기
		ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session
				.getAttribute("shoppingCart");

		// 변동된 상품 수량
		int newProductCount = productCount;

		// 만약 수량을 추가한 것이라면
		if ("upCartProductCount".equals(cartProductCondition)) {
			// 장바구니에서 해당 상품 찾기
			for (Map<String, Object> cartItem : shoppingCart) {
				System.out.println("돌릴 상품 >> " + (int) cartItem.get("productNumber"));
				// 변경을 요청한 상품 번호와 장바구니 안의 상품이 일치한다면
				if ((int) cartItem.get("productNumber") == cartProductNumber) {
					int currentCount = (int) cartItem.get("cartProductCount");
					cartItem.put("cartProductCount", currentCount + newProductCount);
					System.out.println("기존 수량 [" + currentCount + "]");
					System.out.println("바뀐 수량 [" + currentCount + newProductCount + "]");
					return "true";
				}
			}
		} else if ("downCartProductCount".equals(cartProductCondition)) {
			// 장바구니에서 해당 상품 찾기
			for (Map<String, Object> cartItem : shoppingCart) {
				if ((int) cartItem.get("productNumber") == cartProductNumber) {
					int currentCount = (int) cartItem.get("cartProductCount");
					cartItem.put("cartProductCount", currentCount - newProductCount);
					return "true";
				}
			}
		}
		
		// 조건에 맞는 상품을 못 찾았거나 요청이 잘못된 경우
		System.out.println("조건 불일치 또는 상품 번호 없음");
		return "false";
	}

	// 장바구니 상품 삭제 기능
	@PostMapping("/member/cart/delete")
	@ResponseBody
	public String deleteCartProduct(@RequestParam String cartProductNumberDatas, HttpSession session) {
		System.out.println("장바구니 상품 삭제 컨트롤러 진입");

		ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session
				.getAttribute("shoppingCart");

		// 배열로 보낸 상품 번호 로그
		System.out.println(cartProductNumberDatas);

		// 만약 보낸 값이 null 이거나 isEmpty가 아니라면
		if (cartProductNumberDatas != null && !cartProductNumberDatas.isEmpty()) {
			// + 기준으로 상품 번호 분리
			String[] productNumbers = cartProductNumberDatas.split("\\+");
			// 번호를 저장할 Integer List 생성
			ArrayList<Integer> productNumberList = new ArrayList<>();
			// 번호를 하나씩 돌리면서
			for (String productNumber : productNumbers) {
				try {
					// 생성한 ArrayList에 저장 // 문자열 정수 변환
					productNumberList.add(Integer.parseInt(productNumber.trim()));
				} catch (NumberFormatException e) {
					System.err.println("잘못된 상품 번호: [" + productNumber + "]");
				}
			}
			System.out.println("삭제할 상품 번호 리스트: [" + productNumberList + "]");

			// 상품 삭제 로직 추가 (Iterator 사용)
			Iterator<Map<String, Object>> iterator = shoppingCart.iterator();
			// 다음 데이터가 있다면
			while (iterator.hasNext()) {
				Map<String, Object> cartItem = iterator.next();
				// 장바구니 상품 번호
				int cartProductNumber = (int) cartItem.get("productNumber");

				// 리스트에 포함된 경우 삭제
				if (productNumberList.contains(cartProductNumber)) {
					iterator.remove();
					System.out.println("삭제된 상품 번호: " + cartProductNumber);
				}
			}
		}

		// 세션에 상품 삭제 후 변경된 장바구니 저장
		session.setAttribute("shoppingCart", shoppingCart);

		return "true";
	}
}
