package zic.honeyComboFactory.biz.purchaseDetail.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailService;
import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailVO;

@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl implements PurchaseDetailService { // 주문상세 서비스
	@Autowired // PurchaseDetailDAO 객체가 메모리에 new 되어있어야 가능
	private OraclePurchaseDetailDAOMybatis purchaseDetailDAO; // Oracle DB
	// private MySQLPurchaseDetailDAO purchaseDetailDAO; // MySql DB

	@Override
	public List<PurchaseDetailVO> getAll(PurchaseDetailVO purchaseDetailVO) {
		return this.purchaseDetailDAO.getAll(purchaseDetailVO);
	}
	
	@Override
	public PurchaseDetailVO getOne(PurchaseDetailVO purchaseDetailVO) {
		return this.purchaseDetailDAO.getOne(purchaseDetailVO);
	}
	
	@Override
	public boolean insert(PurchaseDetailVO purchaseDetailVO) {
		return this.purchaseDetailDAO.insert(purchaseDetailVO);
	}

	@Override
	public boolean update(PurchaseDetailVO purchaseDetailVO) {
		return this.purchaseDetailDAO.update(purchaseDetailVO);
	}

	@Override
	public boolean delete(PurchaseDetailVO purchaseDetailVO) {
		return this.purchaseDetailDAO.delete(purchaseDetailVO);
	}
}
