package zic.honeyComboFactory.biz.purchaseDetailVO;

import java.util.List;

public interface PurchaseDetailService { // 주문상세 인터페이스
	List<PurchaseDetailVO> getAll(PurchaseDetailVO purchaseDetailVO); // SelectAll()
	PurchaseDetailVO getOne(PurchaseDetailVO purchaseDetailVO); // SelectOne()
	
	boolean insert(PurchaseDetailVO purchaseDetailVO);
	boolean update(PurchaseDetailVO purchaseDetailVO);
	boolean delete(PurchaseDetailVO purchaseDetailVO);
}
