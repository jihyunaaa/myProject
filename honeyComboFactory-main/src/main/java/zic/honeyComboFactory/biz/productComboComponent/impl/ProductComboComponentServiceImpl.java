package zic.honeyComboFactory.biz.productComboComponent.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.productComboComponentVO.ProductComboComponentService;
import zic.honeyComboFactory.biz.productComboComponentVO.ProductComboComponentVO;

@Service("productComboComponentService")
public class ProductComboComponentServiceImpl implements ProductComboComponentService { // 꿀조합 상품 구성품 서비스
	@Autowired // ProductComboComponentDAO 객체가 메모리에 new 되어있어야 가능
	private OracleProductComboComponentDAOMybatis productComboComponentDAO; // Oracle DB
	// private MySQLProductComboComponentDAO productComboComponentDAO; // MySql DB

	@Override
	public List<ProductComboComponentVO> getAll(ProductComboComponentVO productComboComponentVO) {
		return this.productComboComponentDAO.getAll(productComboComponentVO);
	}
	
	@Override
	public ProductComboComponentVO getOne(ProductComboComponentVO productComboComponentVO) {
		return this.productComboComponentDAO.getOne(productComboComponentVO);
	}
	
	@Override
	public boolean insert(ProductComboComponentVO productComboComponentVO) {
		return this.productComboComponentDAO.insert(productComboComponentVO);
	}

	@Override
	public boolean update(ProductComboComponentVO productComboComponentVO) {
		return this.productComboComponentDAO.update(productComboComponentVO);
	}

	@Override
	public boolean delete(ProductComboComponentVO productComboComponentVO) {
		return this.productComboComponentDAO.delete(productComboComponentVO);
	}
}
