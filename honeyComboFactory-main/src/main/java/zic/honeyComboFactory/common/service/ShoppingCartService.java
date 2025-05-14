package zic.honeyComboFactory.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class ShoppingCartService { // 장바구니 서비스
	public void updateShoppingCart(HttpSession session, List<Map<String, Object>> orderList) {
		// 장바구니에서 결제한 상품 제거
		ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session
				.getAttribute("shoppingCart");
		System.out.println("주문 상품 제거 전 장바구니 정보 : [" + shoppingCart + "]");

		// 장바구니와 주문상품 번호 비교하여 같으면 삭제
		ArrayList<String> orderedNumbers = new ArrayList<>();

		for (Map<String, Object> orderedItem : orderList) {
			orderedNumbers.add(orderedItem.get("productNumber").toString());
		}
		shoppingCart.removeIf(item -> orderedNumbers.contains(item.get("productNumber").toString()));
		System.out.println("주문 상품 제거 후 장바구니 정보 : [" + shoppingCart + "]");

		// 장바구니 새로 세션 저장
		session.setAttribute("shoppingCart", shoppingCart);
	}
}
