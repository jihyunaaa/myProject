package zic.honeyComboFactory.biz.productCombo.impl;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.productComboVO.ProductComboVO;

@Repository("oracleProductComboDAO")
public class OracleProductComboDAOMybatis { // 꿀조합 상품 기능 - Oracle DB

	@Autowired
	private SqlSessionTemplate mybatis;

	// getAll → R
	public List<ProductComboVO> getAll(ProductComboVO productComboVO) {

		// .xml 파일 동적 쿼리문 사용
		if (productComboVO.getCondition() != null) {
			System.out.println("ProductCombo >> SELECTALLDYNAMIC <<호출됨");
			return mybatis.selectList("ProductComboDAO.SELECTALLDYNAMIC", productComboVO);
		}
		// 조건에 안맞다면 빈 배열 반환
		else {
			return new ArrayList<>();
		}
	}

	// getOne → R
	public ProductComboVO getOne(ProductComboVO productComboVO) {
		// 조합 상품 상세 조회
		if (productComboVO.getCondition().equals("SELECTONE")) {
			System.out.println("조합 상품 상세 조회");
			return mybatis.selectOne("ProductComboDAO.SELECTONE", productComboVO);
		}
		// 상단 광고 MD픽
		else if (productComboVO.getCondition().equals("SELECTONEADVERTISEMENT")) {
			System.out.println("광고 상품 조회");
			return mybatis.selectOne("ProductComboDAO.SELECTONEADVERTISEMENT", productComboVO);
		}
		// 콤보 상품 판매가격 및 이름
		else if (productComboVO.getCondition().equals("SELECTONECOMBODISCOUNTEDPRICEANDCOMBONAME")) {
			System.out.println("콤보 상품 판매가격 및 이름");
			return mybatis.selectOne("ProductComboDAO.SELECTONECOMBODISCOUNTEDPRICEANDCOMBONAME", productComboVO);
		}
		// 조건에 만족하지 못하면 null 반환
		else {
			return null;
		}
	}

	// insert → C
	public boolean insert(ProductComboVO productComboVO) {
		return false;
	}

	// update → U
	public boolean update(ProductComboVO productComboVO) {
		System.out.println("조합 상품 정보 수정");
		int result = mybatis.update("ProductComboDAO.UPDATE", productComboVO);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// delete → D
	public boolean delete(ProductComboVO productComboVO) {
		System.out.println("조합 상품 삭제");
		// 조합 상품 삭제
		int result = mybatis.delete("ProductComboDAO.DELETE", productComboVO);
		if (result <= 0) {
			return false;
		}
		return true;
	}
}
