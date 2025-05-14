package zic.honeyComboFactory.biz.purchase.impl;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.purchaseVO.PurchaseVO;

@Repository("oraclePurchaseDAO")
public class OraclePurchaseDAOMybatis { // 주문 기능 - Oracle DB

	@Autowired
	private SqlSessionTemplate mybatis;

	// getAll → R
	public List<PurchaseVO> getAll(PurchaseVO purchaseVO) {

		// 주문 목록 전체 출력(최신순)
		if (purchaseVO.getCondition().equals("SELECTALL")) {
			System.out.println("주문 목록 전체 출력(최신순)");
			return mybatis.selectList("PurchaseDAO.SELECTALL", purchaseVO);
		}
		// 회원의 주문 목록 전체 출력
		else if (purchaseVO.getCondition().equals("SELECTALLONEPERSON")) {
			System.out.println("회원의 주문 목록 전체 출력");
			return mybatis.selectList("PurchaseDAO.SELECTALLONEPERSON", purchaseVO);
		}
		// 조건에 안맞다면 빈 배열 반환
		else {
			return new ArrayList<>();
		}
	}

	// getOne → R
	public PurchaseVO getOne(PurchaseVO purchaseVO) {
		System.out.println("주문 정보 상세 조회");
		return mybatis.selectOne("PurchaseDAO.SELECTONE", purchaseVO);
	}

	// insert → C
	public boolean insert(PurchaseVO purchaseVO) {
		System.out.println("주문 등록");
		// 주문 등록
		int result = mybatis.insert("PurchaseDAO.INSERT", purchaseVO);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// update → U
	public boolean update(PurchaseVO purchaseVO) {
		return false;
	}

	// delete → D
	public boolean delete(PurchaseVO purchaseVO) {
		int result = 0;
		// 주문 취소
		if (purchaseVO.getCondition().equals("DELETEPURCHASE")) {
			System.out.println("주문 취소");
			result = mybatis.delete("PurchaseDAO.DELETEPURCHASE", purchaseVO);
		}
		// 탈퇴한 회원의 주문목록 삭제
		else if (purchaseVO.getCondition().equals("DELETECANCELMEMBER")) {
			System.out.println("탈퇴한 회원의 주문목록 삭제");
			result = mybatis.delete("PurchaseDAO.DELETECANCELMEMBER", purchaseVO);
		}

		if (result <= 0) {
			return false;
		}
		return true;
	}

	// 시퀀스 값 조회(주문번호 생성용)
	public int getNextPurchaseSequence() {
		return mybatis.selectOne("PurchaseDAO.GETNEXTPURCHASENUMBER");
	}
}
