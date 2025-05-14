package zic.honeyComboFactory.biz.productComboComponent.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import zic.honeyComboFactory.biz.productComboComponentVO.ProductComboComponentVO;

public class OracleProductComboComponentDAOJdbcTemplate { // 꿀조합 상품 구성품 기능 - Oracle DB

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 1. 꿀조합 구성품 전체 조회
	final String SELECTALL = "SELECT PRODUCT_COMBO_COMPONENT_NUMBER, PRODUCT_COMBO_COMPONENT_ONE, PRODUCT_COMBO_COMPONENT_TWO,"
			+ " PRODUCT_COMBO_COMPONENT_THREE, PRODUCT_COMBO_NUMBER, TOTAL_COUNT_NUMBER"
			+ " FROM (SELECT PRODUCT_COMBO_COMPONENT_NUMBER, PRODUCT_COMBO_COMPONENT_ONE, PRODUCT_COMBO_COMPONENT_TWO,"
			+ " PRODUCT_COMBO_COMPONENT_THREE, PRODUCT_COMBO_NUMBER, (SELECT COUNT(PRODUCT_COMBO_COMPONENT_NUMBER)"
			+ " FROM PRODUCT_COMBO_COMPONENT) AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY PRODUCT_COMBO_COMPONENT_NUMBER DESC)"
			+ " RN FROM PRODUCT_COMBO_COMPONENT) WHERE RN BETWEEN ? AND ?";

	// 2. 꿀조합 구성품 추가
	final String INSERT = "INSERT INTO PRODUCT_COMBO_COMPONENT (PRODUCT_COMBO_COMPONENT_NUMBER, PRODUCT_COMBO_COMPONENT_ONE, PRODUCT_COMBO_COMPONENT_TWO,"
			+ " PRODUCT_COMBO_COMPONENT_THREE, PRODUCT_COMBO_NUMBER) VALUES (SEQ_PRODUCT_COMBO_COMPONENT.NEXTVAL, ?, ?, ?, ?)";

	// 3. 꿀조합 구성품 삭제
	final String DELETE = "DELETE FROM PRODUCT_COMBO_COMPONENT WHERE PRODUCT_COMBO_COMPONENT_NUMBER = ?";

	// 4. 조합 구성품 출력
	final String SELECTALLCOMPONENT = "SELECT PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE.PRODUCT_SINGLE_NAME,"
			+ " PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE, ROUND(PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE * (1 - NVL(PRODUCT_SINGLE.PRODUCT_SINGLE_DISCOUNT, 0) / 100), 0)"
			+ " AS SINGLE_DISCOUNTED_PRICE FROM PRODUCT_COMBO_COMPONENT LEFT JOIN PRODUCT_SINGLE"
			+ " ON PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_ONE"
			+ " OR PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_TWO"
			+ " OR PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_THREE"
			+ " WHERE PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_NUMBER = ?";

	// 5. 꿀조합 상품 구성품 번호만 정보
	final String SELECTONECOMPONENTNUMBERONLY = "SELECT PRODUCT_COMBO_COMPONENT_ONE, PRODUCT_COMBO_COMPONENT_TWO, PRODUCT_COMBO_COMPONENT_THREE"
			+ " FROM PRODUCT_COMBO_COMPONENT WHERE PRODUCT_COMBO_NUMBER = ?";

	// getAll → R
	public List<ProductComboComponentVO> getAll(ProductComboComponentVO productComboComponentVO) {

		// 조합 구성품 전체 조회
		if (productComboComponentVO.getCondition().equals("SELECTALL")) {
			System.out.println("조합 구성품 전체 조회");
			Object[] args = { productComboComponentVO.getProductComboComponentIndex(),
					productComboComponentVO.getProductComboComponentContentCount() };
			return jdbcTemplate.query(SELECTALL, args, new ProductComboComponentGetAllRowMapper());
		}
		// 조합 구성품 조회
		else if (productComboComponentVO.getCondition().equals("SELECTALLCOMPONENT")) {
			System.out.println("조합 구성품 조회");
			Object[] args = { productComboComponentVO.getProductComboNumber() };
			return jdbcTemplate.query(SELECTALLCOMPONENT, args, new ProductComboComponentGetComponentRowMapper());
		}
		// 조건에 만족하지 못하면 빈 배열 반환
		else {
			return new ArrayList<>();
		}
	}

	// getOne → R
	public ProductComboComponentVO getOne(ProductComboComponentVO productComboComponentVO) {
		System.out.println("조합 구성품 단일 조회");

		Object[] args = { productComboComponentVO.getProductComboNumber() };
		
		List<ProductComboComponentVO> list = jdbcTemplate.query(SELECTONECOMPONENTNUMBERONLY, args, new ProductComboComponentGetComponentNumberOnlyRowMapper());
		
		return getSingleResult(list);
	}

	// insert → C
	public boolean insert(ProductComboComponentVO productComboComponentVO) {
		System.out.println("조합 구성품 추가");
		Object[] args = { productComboComponentVO.getProductComboComponentOne(),
				productComboComponentVO.getProductComboComponentTwo(),
				productComboComponentVO.getProductComboComponentThree(),
				productComboComponentVO.getProductComboNumber() };
		// 상품 구성품 추가
		int result = jdbcTemplate.update(INSERT, args);

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
		Object[] args = { productComboComponentVO.getProductComboComponentNumber() };
		// 상품 구성품 삭제
		int result = jdbcTemplate.update(DELETE, args);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// 반환 메서드
	private ProductComboComponentVO getSingleResult(List<ProductComboComponentVO> list) {
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}

// 조합 상품 구성품 전체 조회
class ProductComboComponentGetAllRowMapper implements RowMapper<ProductComboComponentVO> {

	@Override
	public ProductComboComponentVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductComboComponentVO data = new ProductComboComponentVO();

		data.setProductComboComponentNumber(rs.getLong("PRODUCT_COMBO_COMPONENT_NUMBER"));
		data.setProductComboComponentOne(rs.getLong("PRODUCT_COMBO_COMPONENT_ONE"));
		data.setProductComboComponentTwo(rs.getLong("PRODUCT_COMBO_COMPONENT_TWO"));
		data.setProductComboComponentThree(rs.getLong("PRODUCT_COMBO_COMPONENT_THREE"));
		data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
		data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));

		System.out.println("getAll 상품 구성품 전체 데이터 [" + data + "]");
		return data;
	}
}

// 조합 구성품 출력
class ProductComboComponentGetComponentRowMapper implements RowMapper<ProductComboComponentVO> {

	@Override
	public ProductComboComponentVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductComboComponentVO data = new ProductComboComponentVO();

		data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
		data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
		data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
		data.setProductSingleDiscountedPrice(rs.getLong("SINGLE_DISCOUNTED_PRICE"));

		System.out.println("getAll 조합 구성품 데이터 [" + data + "]");
		return data;
	}
}

//조합 구성품 번호만 출력
class ProductComboComponentGetComponentNumberOnlyRowMapper implements RowMapper<ProductComboComponentVO> {

	@Override
	public ProductComboComponentVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductComboComponentVO data = new ProductComboComponentVO();

		data.setProductComboComponentOne(rs.getLong("PRODUCT_COMBO_COMPONENT_ONE"));
		data.setProductComboComponentTwo(rs.getLong("PRODUCT_COMBO_COMPONENT_TWO"));
		data.setProductComboComponentThree(rs.getLong("PRODUCT_COMBO_COMPONENT_THREE"));

		System.out.println("DAO getOne-조합 구성품 번호 : [");
		System.out.println(" 구성품1 : " + data.getProductComboComponentOne());
		System.out.println(" 구성품2 : " + data.getProductComboComponentTwo());
		System.out.println(" 구성품3 : " + data.getProductComboComponentThree());
		System.out.println("]");
		return data;
	}
}

//23ai 버전 OFFSET ? ROWS FETCH NEXT ? ROWS ONLY을 사용하였으나 11g에서는 사용 불가

/*
 * // 1. 꿀조합 구성품 전체 조회 final String SELECTALL =
 * "SELECT PRODUCT_COMBO_COMPONENT_NUMBER, PRODUCT_COMBO_COMPONENT_ONE, PRODUCT_COMBO_COMPONENT_TWO, "
 * +
 * "PRODUCT_COMBO_COMPONENT_THREE, PRODUCT_COMBO_NUMBER, PRODUCT_SINGLE_NUMBER, "
 * +
 * "(SELECT COUNT(PRODUCT_COMBO_COMPONENT_NUMBER) FROM PRODUCT_COMBO_COMPONENT) AS TOTAL_COUNT_NUMBER "
 * + "FROM PRODUCT_COMBO_COMPONENT LIMIT ?, ?";
 * 
 * // 2. 꿀조합 구성품 추가 final String INSERT =
 * "INSERT INTO PRODUCT_COMBO_COMPONENT (PRODUCT_COMBO_COMPONENT_NUMBER, PRODUCT_COMBO_COMPONENT_ONE, "
 * +
 * "PRODUCT_COMBO_COMPONENT_TWO, PRODUCT_COMBO_COMPONENT_THREE, PRODUCT_COMBO_NUMBER, PRODUCT_SINGLE_NUMBER) "
 * + "VALUES (?, ?, ?, ?, ?, ?)";
 * 
 * // 3. 꿀조합 구성품 삭제 final String DELETE =
 * "DELETE FROM PRODUCT_COMBO_COMPONENT WHERE PRODUCT_COMBO_COMPONENT_NUMBER = ?"
 * ;
 * 
 * // 4. 조합 구성품 출력 final String SELECTALLCOMPONENT =
 * "SELECT PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE.PRODUCT_SINGLE_NAME,"
 * +
 * " PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE, ROUND(PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE *"
 * +
 * " (1 - NVL(PRODUCT_SINGLE.PRODUCT_SINGLE_DISCOUNT, 0) / 100), 0) AS SINGLE_DISCOUNTED_PRICE FROM PRODUCT_COMBO_COMPONENT"
 * +
 * " LEFT JOIN PRODUCT_SINGLE ON PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_ONE OR"
 * +
 * " PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_TWO OR PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER"
 * +
 * " = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_THREE WHERE PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_NUMBER = ?"
 * ;
 * 
 * 
 */
