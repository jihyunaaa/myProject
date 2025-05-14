package zic.honeyComboFactory.biz.purchaseDetail.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailVO;

public class OraclePurchaseDetailDAOJdbcTemplate { // 주문상세 기능 - Oracle DB

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 일치하는 주문번호에 맞춰서 상품 상세 정보 출력
	final String SELECTALL = "SELECT PURCHASE_DETAIL_NUMBER, PURCHASE_NUMBER, MEMBER_NUMBER, PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE,"
			+ " PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT,"
			+ " COMBO_DISCOUNTED_PRICE, PURCHASE_PRODUCT_COUNT, PURCHASE_TOTAL_PRICE FROM VIEW_PURCHASE_DETAIL WHERE PURCHASE_NUMBER = ?";



	// 주문 상세정보 저장
	final String INSERT = "INSERT INTO PURCHASE_DETAIL (PURCHASE_DETAIL_NUMBER, PURCHASE_PRODUCT_COUNT, PRODUCT_SINGLE_NUMBER, PRODUCT_COMBO_NUMBER,"
			+ " PURCHASE_NUMBER) VALUES (SEQ_PURCHASE_DETAIL.NEXTVAL, ?, ?, ?, ?)";

	// getAll → R
	public List<PurchaseDetailVO> getAll(PurchaseDetailVO purchaseDetailVO) {
		System.out.println("주문 정보 상세 조회");
		// 주문 상세 정보 조회
		Object[] args = { purchaseDetailVO.getPurchaseNumber() };
		return jdbcTemplate.query(SELECTALL, args, new PurchaseDetailGetAllRowMapper());
	}

	// getOne → R
	public PurchaseDetailVO getOne(PurchaseDetailVO purchaseDetailVO) {
		return null;
	}

	// insert → C
	public boolean insert(PurchaseDetailVO purchaseDetailVO) {
		System.out.println("주문 상세 등록");
		Object[] args = { purchaseDetailVO.getPurchaseProductCount(), purchaseDetailVO.getProductSingleNumber(),
				purchaseDetailVO.getProductComboNumber(), purchaseDetailVO.getPurchaseNumber() };
		// 주문 상세 등록
		int result = jdbcTemplate.update(INSERT, args);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// update → U
	public boolean update(PurchaseDetailVO purchaseDetailVO) {
		return false;
	}

	// delete → D
	public boolean delete(PurchaseDetailVO purchaseDetailVO) {
		return false;
	}
}

// 주문 상세 정보 조회
class PurchaseDetailGetAllRowMapper implements RowMapper<PurchaseDetailVO> {

	@Override
	public PurchaseDetailVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		PurchaseDetailVO data = new PurchaseDetailVO();

		data.setPurchaseDetailNumber(rs.getLong("PURCHASE_DETAIL_NUMBER")); // 주문 상세 번호
		data.setPurchaseNumber(rs.getLong("PURCHASE_NUMBER")); // 주문 번호
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER")); // 회원 번호

		data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER")); // 개별 상품 번호
		data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME")); // 개별 상품 이름
		data.setProductSinglePrice(rs.getLong("PRODUCT_SINGLE_PRICE")); // 개별 상품 원가
		data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT")); // 개별 상품 할인율
		data.setProductSingleDiscountedPrice(rs.getInt("SINGLE_DISCOUNTED_PRICE")); // 개별 상품 할인 적용가

		data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER")); // 조합 상품 번호
		data.setProductComboName(rs.getString("PRODUCT_COMBO_NAME")); // 조합 상품 이름
		data.setProductComboPrice(rs.getLong("PRODUCT_COMBO_PRICE")); // 조합 상품 원가
		data.setProductComboDiscount(rs.getInt("PRODUCT_COMBO_DISCOUNT")); // 조합 상품 할인율
		data.setProductComboDiscountedPrice(rs.getInt("COMBO_DISCOUNTED_PRICE")); // 조합 상품 할인 적용가

		data.setPurchaseProductCount(rs.getLong("PURCHASE_PRODUCT_COUNT")); // 구매 수량
		data.setPurchaseTotalPrice(rs.getLong("PURCHASE_TOTAL_PRICE")); // 총 구매 금액


		System.out.println("getAll 주문 상세 데이터 ["+data+"]");
		return data;
	}

}


//23ai 버전 OFFSET ? ROWS FETCH NEXT ? ROWS ONLY을 사용하였으나 11g에서는 사용 불가

/*
 // 일치하는 주문번호에 맞춰서 상품 상세 정보 출력
	final String SELECTALL = "SELECT PURCHASE_DETAIL_NUMBER, PURCHASE_NUMBER, MEMBER_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_COMBO_NAME, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, COMBO_DISCOUNTED_PRICE,"
			+ " PURCHASE_PRODUCT_COUNT, PURCHASE_TOTAL_PRICE FROM VIEW_PURCHASE_DETAIL WHERE MEMBER_NUMBER = ? AND PURCHASE_NUMBER = ?";

	// 주문 상세정보 저장
	final String INSERT = "INSERT INTO PURCHASE_DETAIL (PURCHASE_DETAIL_NUMBER, PURCHASE_PRODUCT_COUNT, PRODUCT_SINGLE_NUMBER,"
			+ " PRODUCT_COMBO_NUMBER, PURCHASE_NUMBER) VALUES (SEQ_PURCHASE_DETAIL.NEXTVAL, ?, ?, ?, ?)";
 */


