package zic.honeyComboFactory.biz.purchaseDetail.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailVO;

@Repository("oraclePurchaseDetailDAO")
public class OraclePurchaseDetailDAOMybatis { // 주문상세 기능 - Oracle DB

	@Autowired
	private SqlSessionTemplate mybatis;
	
	// getAll → R
	public List<PurchaseDetailVO> getAll(PurchaseDetailVO purchaseDetailVO) {
		System.out.println("주문 정보 상세 조회");
		// 주문 상세 정보 조회
		return mybatis.selectList("PurchaseDetailDAO.SELECTALL", purchaseDetailVO);
	}

	// getOne → R
	public PurchaseDetailVO getOne(PurchaseDetailVO purchaseDetailVO) {
		return null;
	}

	// insert → C
	public boolean insert(PurchaseDetailVO purchaseDetailVO) {
		System.out.println("주문 상세 등록");
		// 주문 상세 등록
		int result = mybatis.insert("PurchaseDetailDAO.INSERT", purchaseDetailVO);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// update → U
	public boolean update(PurchaseDetailVO purchaseDetailVO) {
		return false;
	}

	// delete → D
	public boolean delete(PurchaseDetailVO purchaseDetailVO) {
		return false;
	}
}
