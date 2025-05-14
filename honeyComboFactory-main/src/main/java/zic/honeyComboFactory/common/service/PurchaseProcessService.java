package zic.honeyComboFactory.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.purchaseVO.PurchaseVO;

@Service
public class PurchaseProcessService {

	@Autowired
	private ProductService productService;
	@Autowired
	private PurchaseTransactionMybatisService purchaseTransactionMybatisService;
	@Autowired
	private PurchaseDetailTransactionService purchaseDetailTransactionService;
	@Autowired
	private ShoppingCartService shoppingCartService;

	@Transactional
	// 상품 결제 후 DB처리 기능
	public int processPurchase(HttpSession session, List<Map<String, Object>> orderList, long totalAmount,
			PurchaseVO purchaseVO, String tid, String partnerOrderId, String partnerUserId, List<Long> sortedCartProductNumberDatas, String cartProductNumberDatas) {
		try {
			boolean flag = false; // 실패 상태 가정

			// 상품 재고 DB락 걸기
			flag = this.productService.lockProductSingleStockForUpdate(sortedCartProductNumberDatas);
			if (!flag) { // 상품 재고 DB락 걸기 실패 시
				System.out.println("상품 재고 DB락 걸기 실패");
				throw new IllegalStateException("상품 재고 DB락 걸기 실패");
			}
			
			// 상품 재고 검사
			if (!this.productService.checkStock(cartProductNumberDatas, session)) {
				System.out.println("상품 재고 부족");
				throw new IllegalStateException("상품 재고 부족");
			}

			// 재고 변경 처리
			flag = this.productService.updateProductStock(orderList);
			if (!flag) { // 재고 변경 실패 시
				System.out.println("상품 재고 변경 실패");
				throw new IllegalStateException("상품 재고 변경 실패");
			}

			// 주문 정보 DB 저장
			flag = purchaseTransactionMybatisService.savePurchaseInfo(purchaseVO, totalAmount, tid, partnerOrderId, partnerUserId);
			if (!flag) { // 주문 정보 저장 실패 시
				System.out.println("주문정보 저장 실패");
				throw new IllegalStateException("주문정보 저장 실패");
			}

			// 주문상세 정보 DB 저장
			flag = purchaseDetailTransactionService.insertPurchaseDetailInfo(orderList, partnerOrderId, partnerUserId);
			if (!flag) { // 주문상세 정보 저장 실패 시
				System.out.println("주문상세 정보 저장 실패");
				throw new IllegalStateException("주문상세 정보 저장 실패");
			}

			// 장바구니에서 결제한 상품 제거
			this.shoppingCartService.updateShoppingCart(session, orderList);

			return 0;
		} catch (Exception e) {
			System.out.println("예외 발생: " + e.getMessage());
			// 재고 부족의 경우
			if(e.equals("상품 재고 부족")) {
				return 2;
			}
			return 1;
		}
	}
}