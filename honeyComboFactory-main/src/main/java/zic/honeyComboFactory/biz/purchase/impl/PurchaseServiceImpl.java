package zic.honeyComboFactory.biz.purchase.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.purchaseVO.PurchaseService;
import zic.honeyComboFactory.biz.purchaseVO.PurchaseVO;

@Service("purchaseService")
public class PurchaseServiceImpl implements PurchaseService { // 주문 서비스
	@Autowired // PurchaseDAO 객체가 메모리에 new 되어있어야 가능
	private OraclePurchaseDAOMybatis purchaseDAO; // Oracle DB
	// private MySQLPurchaseDAO purchaseDAO; // MySql DB

	@Override
	public List<PurchaseVO> getAll(PurchaseVO purchaseVO) {
		return this.purchaseDAO.getAll(purchaseVO);
	}
	
	@Override
	public PurchaseVO getOne(PurchaseVO purchaseVO) {
		return this.purchaseDAO.getOne(purchaseVO);
	}
	
	@Override
	public boolean insert(PurchaseVO purchaseVO) {
		return this.purchaseDAO.insert(purchaseVO);
	}

	@Override
	public boolean update(PurchaseVO purchaseVO) {
		return this.purchaseDAO.update(purchaseVO);
	}

	@Override
	public boolean delete(PurchaseVO purchaseVO) {
		return this.purchaseDAO.delete(purchaseVO);
	}
}
