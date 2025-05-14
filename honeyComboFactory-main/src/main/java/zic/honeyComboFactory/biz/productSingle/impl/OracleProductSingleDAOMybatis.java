package zic.honeyComboFactory.biz.productSingle.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;

@Repository("oracleProductSingleDAO")
public class OracleProductSingleDAOMybatis { // 개별 상품 기능 - Oracle DB

	@Autowired
	private SqlSessionTemplate mybatis;

	// getAll → R
	public List<ProductSingleVO> getAll(ProductSingleVO productSingleVO) {
		// .xml 파일 동적 쿼리문 사용
		if(productSingleVO.getCondition() != null) {
			System.out.println("ProductSingle >>SELECTALLDYNAMIC<< 호출됨");
			return mybatis.selectList("ProductSingleDAO.SELECTALLDYNAMIC", productSingleVO);
		}
		// 조건에 안맞다면 빈 배열 반환
		else {
			return new ArrayList<>();
		}
	}

	// getOne → R
	public ProductSingleVO getOne(ProductSingleVO productSingleVO) {

		// 상품 상세정보
		if (productSingleVO.getCondition().equals("SELECTONE")) {
			System.out.println("상품 상세정보");
			return mybatis.selectOne("ProductSingleDAO.SELECTONE", productSingleVO);
		}
		// 개별 상품 판매 가격 및 이름
		if (productSingleVO.getCondition().equals("SELECTONESINGLEDISCOUNTEDPRICEANDSINGLENAME")) {
			System.out.println("개별 상품 가격 및 이름");
			return mybatis.selectOne("ProductSingleDAO.SELECTONESINGLEDISCOUNTEDPRICEANDSINGLENAME", productSingleVO);
		}
		// 개별상품 재고 및 카테고리 정보
		if (productSingleVO.getCondition().equals("SELECTONESTOCKANDCATEGORY")) {
			System.out.println("개별상품 재고 및 카테고리 정보");
			return mybatis.selectOne("ProductSingleDAO.SELECTONESTOCKANDCATEGORY", productSingleVO);
		}
		// 개별상품 DB락
		if(productSingleVO.getCondition().equals("LOCKPRODUCTSINGLESTOCKFORUPDATE")) {
			System.out.println("결제 전 DB락");
			System.out.println("DB락 걸 상품 번호 ["+productSingleVO.getProductSingleNumber()+"]");
			return mybatis.selectOne("ProductSingleDAO.LOCKPRODUCTSINGLESTOCKFORUPDATE", productSingleVO);
		}
		// 조건에 만족하지 못하면 null 반환
		else {
			return null;
		}
	}

	// insert → C
	public boolean insert(ProductSingleVO productSingleVO) {
	    System.out.println("상품 등록");

	    // 중복 상품 확인
	    if (existsByName(productSingleVO.getProductSingleName())) {
	        System.out.println("[DAO/insert] 중복 상품 등록 생략: " + productSingleVO.getProductSingleName());
	        return false;
	    }

	    // 상품번호 자동 생성
	    if (productSingleVO.getProductSingleNumber() == 0) {
	        productSingleVO.setProductSingleNumber(getNextProductSingleNumberByBrand(productSingleVO.getProductSingleStore()));
	    }

	    // MyBatis insert 수행
	    int result = mybatis.insert("ProductSingleDAO.INSERT", productSingleVO);

	    if (result <= 0) {
	        System.out.println("상품 등록 실패");
	        return false;
	    }

	    System.out.println("상품 등록 성공");
	    return true;
	}
	/*
	 * 상품명이 DB에 이미 존재하는지 확인하는 메서드
	 * - 중복된 상품명이 있으면 true 반환 (insert 차단)
	 * - 예외 발생 시에도 true 반환 (안전하게 insert 차단)
	 *
	 * @param productName 확인할 상품명
	 * @return true: 중복 또는 예외 발생 → insert 차단 / false: 중복 아님 → insert 가능
	 */
	private boolean existsByName(String productName) {
	    try {
	        Map<String, Object> param = new HashMap<>();
	        param.put("productSingleName", productName);

	        int count = mybatis.selectOne("ProductSingleDAO.EXISTSBYNAME", param);
	        System.out.println("[DAO/existsByName] 상품명 '" + productName + "' 중복 개수: " + count);
	        return count > 0;
	    } catch (Exception e) {
	        System.err.println("[DAO/existsByName] 예외 발생, insert 차단 (상품명: " + productName + ")");
	        return true; // 예외 발생 시 insert 차단
	    }
	}

	private static final Map<String, Long> storeMap = new HashMap<>();
	static {
		storeMap.put("CU", 10000L);// cu는 10000~
		storeMap.put("GS25", 20000L);// gs25는 20000~
		// 확장성을 위해 다른 store도 추가 쉽게 할 수 있음
	}
	private long getNextProductSingleNumberByBrand(String store) {
	    long base = storeMap.getOrDefault(store, 30000L); // 기본 시작 번호
	    long maxRange = base + 9999;                      // 해당 브랜드 최대 번호
	    long nextNumber = base;

	    try {
	        Map<String, Object> param = new HashMap<>();
	        param.put("base", base);
	        param.put("maxRange", maxRange);
	        param.put("defaultValue", base - 1); // NVL에서 사용될 기본값

	        Long max = mybatis.selectOne("ProductSingleDAO.GETMAXPRODUCTSINGLENUMBERBYBRAND", param);

	        if (max != null) {
	            nextNumber = (max == 0) ? base : max + 1;
	            System.out.println("[DAO/getNextProductSingleNumberByBrand] 브랜드: " + store + ", 최대 번호: " + max + ", 다음 번호: " + nextNumber);
	        } else {
	            System.out.println("[DAO/getNextProductSingleNumberByBrand] 최대 번호 결과 null, 기본값 사용");
	        }
	    } catch (Exception e) {
	        System.err.println("[DAO/getNextProductSingleNumberByBrand] 예외 발생 - 브랜드: " + store);
	        e.printStackTrace();
	    }
	    return nextNumber;
	}

	// update → U
	public boolean update(ProductSingleVO productSingleVO) {
		int result = 0;
		// 개별 상품 재고 변경
		if (productSingleVO.getCondition().equals("UPDATESTOCKDECREASEONLY")) {
			System.out.println("개별 상품 재고 변경");
			result = mybatis.update("ProductSingleDAO.UPDATESTOCKDECREASEONLY", productSingleVO);
		}

		// 실행된 쿼리가 0 이하라면
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// delete → D
	public boolean delete(ProductSingleVO productSingleVO) {
		return false;
	}
}
