package zic.honeyComboFactory.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailService;
import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailVO;

@Service
public class PurchaseDetailTransactionService {

	@Autowired
	private PurchaseDetailService purchaseDetailService;

	// 주문상세 정보 DB 저장 기능
	public boolean insertPurchaseDetailInfo(List<Map<String, Object>> orderList, String partnerOrderId,
			String partnerUserId) {
		for (Map<String, Object> item : orderList) {
			PurchaseDetailVO purchaseDetailVO = createPurchaseDetailVO(item, partnerOrderId, partnerUserId);
			System.out.println("저장할 주문 상세정보 : [");
			System.out.println("주문 번호 : "+purchaseDetailVO.getPurchaseNumber());
			System.out.println("구매 수량 : "+purchaseDetailVO.getPurchaseProductCount());
			System.out.println("개별상품 번호 : "+purchaseDetailVO.getProductSingleNumber());
			System.out.println("꿀조합상품 번호 : "+purchaseDetailVO.getProductComboNumber());
			System.out.println("]");
			boolean flag = this.purchaseDetailService.insert(purchaseDetailVO);
			// 하나라도 저장 실패 시
			if (!flag) {
				return false;
			}
		}
		return true;
	}

	// 주문상세 저장할 객체 생성 기능
	private PurchaseDetailVO createPurchaseDetailVO(Map<String, Object> item, String purchaseNumber,
			String memberNumber) {
		// 주문상세 정보 세팅
		PurchaseDetailVO purchaseDetailVO = new PurchaseDetailVO();
		purchaseDetailVO.setCondition("INSERT");
		purchaseDetailVO.setPurchaseNumber(Long.parseLong(purchaseNumber));
		purchaseDetailVO.setPurchaseProductCount(Long.parseLong(item.get("count").toString()));
		purchaseDetailVO.setMemberNumber(Long.parseLong(memberNumber));

		boolean isComboProduct = Boolean.parseBoolean(item.get("isComboProduct").toString());
		// 꿀조합 상품이라면
		if (isComboProduct) {
			purchaseDetailVO.setProductComboNumber(Long.parseLong(item.get("productNumber").toString()));
		}
		// 개별 상품이라면
		else {
			purchaseDetailVO.setProductSingleNumber(Long.parseLong(item.get("productNumber").toString()));
		}
		return purchaseDetailVO;
	}
}
