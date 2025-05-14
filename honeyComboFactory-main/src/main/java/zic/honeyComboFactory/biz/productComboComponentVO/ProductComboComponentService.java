package zic.honeyComboFactory.biz.productComboComponentVO;

import java.util.List;

public interface ProductComboComponentService { // 꿀조합 상품 구성품 인터페이스
	List<ProductComboComponentVO> getAll(ProductComboComponentVO productComboComponentVO); // SelectAll()
	ProductComboComponentVO getOne(ProductComboComponentVO ProductComboComponentVO); // SelectOne()
	
	boolean insert(ProductComboComponentVO ProductComboComponentVO);
	boolean update(ProductComboComponentVO ProductComboComponentVO);
	boolean delete(ProductComboComponentVO ProductComboComponentVO);
}
