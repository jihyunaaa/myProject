package zic.honeyComboFactory.biz.productSingle.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.productSingleVO.ProductSingleService;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;

@Service("productSingleService")
public class ProductSingleServiceImpl implements ProductSingleService { // 개별 상품 서비스
	@Autowired // ProductSingleDAO 객체가 메모리에 new 되어있어야 가능
	private OracleProductSingleDAOMybatis productSingleDAO; // Oracle DB
	// private MySQLProductSingleDAO productSingleDAO; // MySql DB

	@Override
	public List<ProductSingleVO> getAll(ProductSingleVO productSingleVO) {
		return this.productSingleDAO.getAll(productSingleVO);
	}
	
	@Override
	public ProductSingleVO getOne(ProductSingleVO productSingleVO) {
		return this.productSingleDAO.getOne(productSingleVO);
	}
	
	@Override
	public boolean insert(ProductSingleVO productSingleVO) {
		return this.productSingleDAO.insert(productSingleVO);
	}

	@Override
	public boolean update(ProductSingleVO productSingleVO) {
		return this.productSingleDAO.update(productSingleVO);
	}

	@Override
	public boolean delete(ProductSingleVO productSingleVO) {
		return this.productSingleDAO.delete(productSingleVO);
	}
}
