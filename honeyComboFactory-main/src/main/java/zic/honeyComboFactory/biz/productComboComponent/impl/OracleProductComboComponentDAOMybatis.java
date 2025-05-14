package zic.honeyComboFactory.biz.productComboComponent.impl;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.productComboComponentVO.ProductComboComponentVO;

@Repository("oracleProductComboComponentDAO")
public class OracleProductComboComponentDAOMybatis { // 꿀조합 상품 구성품 기능 - Oracle DB

	@Autowired
	private SqlSessionTemplate mybatis;

	// getAll → R
	public List<ProductComboComponentVO> getAll(ProductComboComponentVO productComboComponentVO) {

		// 조합 구성품 전체 조회
		if (productComboComponentVO.getCondition().equals("SELECTALL")) {
			System.out.println("조합 구성품 전체 조회");
			return mybatis.selectList("ProductComboComponentDAO.SELECTALL", productComboComponentVO);
		}
		// 조합 구성품 조회
		else if (productComboComponentVO.getCondition().equals("SELECTALLCOMPONENT")) {
			System.out.println("조합 구성품 조회");
			return mybatis.selectList("ProductComboComponentDAO.SELECTALLCOMPONENT", productComboComponentVO);
		}
		// 조건에 만족하지 못하면 빈 배열 반환
		else {
			return new ArrayList<>();
		}
	}

	// getOne → R
	public ProductComboComponentVO getOne(ProductComboComponentVO productComboComponentVO) {
		System.out.println("조합 구성품 단일 조회");
		return mybatis.selectOne("ProductComboComponentDAO.SELECTONECOMPONENTNUMBERONLY", productComboComponentVO);
	}

	// insert → C
	public boolean insert(ProductComboComponentVO productComboComponentVO) {
		System.out.println("조합 구성품 추가");
		// 상품 구성품 추가
		int result = mybatis.insert("ProductComboComponentDAO.INSERT", productComboComponentVO);

		if (result <= 0) {
			return false;
		}
		return true;
	}

	// update → U
	public boolean update(ProductComboComponentVO productComboComponentVO) {
		return false;
	}

	// delete → D
	public boolean delete(ProductComboComponentVO productComboComponentVO) {
		System.out.println("상품 구성품 삭제");
		// 상품 구성품 삭제
		int result = mybatis.delete("ProductComboComponentDAO.DELETE", productComboComponentVO);
		if (result <= 0) {
			return false;
		}
		return true;
	}
}