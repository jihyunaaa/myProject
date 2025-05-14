package zic.honeyComboFactory.biz.productCombo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.productComboVO.ProductComboService;
import zic.honeyComboFactory.biz.productComboVO.ProductComboVO;

@Service("productComboService")
public class ProductComboServiceImpl implements ProductComboService { // 꿀조합 상품 서비스
	@Autowired // ProductComboDAO 객체가 메모리에 new 되어있어야 가능
	private OracleProductComboDAOMybatis productComboDAO; // Oracle DB
	// private MySQLProductComboDAO productComboDAO; // MySql DB

	@Override
	public List<ProductComboVO> getAll(ProductComboVO productComboVO) {
		return this.productComboDAO.getAll(productComboVO);
	}
	
	@Override
	public ProductComboVO getOne(ProductComboVO productComboVO) {
		return this.productComboDAO.getOne(productComboVO);
	}
	
	@Override
	public boolean insert(ProductComboVO productComboVO) {
		return this.productComboDAO.insert(productComboVO);
	}

	@Override
	public boolean update(ProductComboVO productComboVO) {
		return this.productComboDAO.update(productComboVO);
	}

	@Override
	public boolean delete(ProductComboVO productComboVO) {
		return this.productComboDAO.delete(productComboVO);
	}
}
