package zic.honeyComboFactory.biz.purchaseVO;

import java.util.List;

public interface PurchaseService { // 주문 인터페이스
	List<PurchaseVO> getAll(PurchaseVO purchaseVO); // SelectAll()
	PurchaseVO getOne(PurchaseVO purchaseVO); // SelectOne()
	
	boolean insert(PurchaseVO purchaseVO);
	boolean update(PurchaseVO purchaseVO);
	boolean delete(PurchaseVO purchaseVO);
}
