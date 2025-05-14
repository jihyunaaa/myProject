package zic.honeyComboFactory.common.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.purchase.impl.OraclePurchaseDAOMybatis;
import zic.honeyComboFactory.biz.purchaseVO.PurchaseService;
import zic.honeyComboFactory.biz.purchaseVO.PurchaseVO;

@Service
public class PurchaseTransactionMybatisService { // 주문 서비스

	@Autowired
	private OraclePurchaseDAOMybatis purchaseDAO;
	@Autowired
	private PurchaseService purchaseService;

	// 주문 정보 저장 기능
	public boolean savePurchaseInfo(PurchaseVO purchaseVO, long totalAmount, String tid,
			String purchaseNumber, String memberNumber) {
		// 주문 정보 DB 저장
		purchaseVO.setPurchaseNumber(Long.parseLong(purchaseNumber));
		purchaseVO.setPurchaseTerminalId(tid);
		purchaseVO.setPurchaseTotalPrice(totalAmount);
		purchaseVO.setMemberNumber(Long.parseLong(memberNumber));
		System.out.println("저장할 주문정보 : [");
		System.out.println("주문 번호 : "+purchaseVO.getPurchaseNumber());
		System.out.println("결제 번호 : "+purchaseVO.getPurchaseTerminalId());
		System.out.println("총 금액 : "+purchaseVO.getPurchaseTotalPrice());
		System.out.println("회원 번호 : "+purchaseVO.getMemberNumber());
		System.out.println("]");

		boolean flag = this.purchaseService.insert(purchaseVO);
		// 주문 정보 저장 실패 시
		if (!flag) {
			return false;
		}
		return true;
	}

	// 주문번호 생성
	public Long generatePurchaseNumber() {
		// 오늘 날짜
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());

		// DB에서 시퀀스 값 받아오기
		int seq = this.purchaseDAO.getNextPurchaseSequence();

		// 날짜 + 시퀀스를 조합한 주문번호
		// format("%03d", seq)으로 001처럼 세자리 수 만듬
		String orderNumberStr = today + String.format("%03d", seq);
		return Long.parseLong(orderNumberStr);
	}
}
