package zic.honeyComboFactory.biz.productSingleVO;

import java.util.List;

public interface ProductSingleService { // 개별 상품 인터페이스
	List<ProductSingleVO> getAll(ProductSingleVO productSingleVO); // SelectAll()
	ProductSingleVO getOne(ProductSingleVO productSingleVO); // SelectOne()
	
	boolean insert(ProductSingleVO productSingleVO);
	boolean update(ProductSingleVO productSingleVO);
	boolean delete(ProductSingleVO productSingleVO);
}
