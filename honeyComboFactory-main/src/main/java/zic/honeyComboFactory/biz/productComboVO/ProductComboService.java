package zic.honeyComboFactory.biz.productComboVO;

import java.util.List;

public interface ProductComboService { // 꿀조합 상품 인터페이스
	List<ProductComboVO> getAll(ProductComboVO productComboVO); // SelectAll()
	ProductComboVO getOne(ProductComboVO productComboVO); // SelectOne()
	
	boolean insert(ProductComboVO productComboVO);
	boolean update(ProductComboVO productComboVO);
	boolean delete(ProductComboVO productComboVO);
}
