package zic.honeyComboFactory.biz.purchase.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import zic.honeyComboFactory.biz.purchaseVO.PurchaseVO;

public class OraclePurchaseDAOJdbcTemplate { // 주문 기능 - Oracle DB

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 1. 모든 주문 목록 출력 (최신순)
	final String SELECTALL = "SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER, TOTAL_COUNT_NUMBER"
			+ " FROM (SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER, ROW_NUMBER() OVER"
			+ " (ORDER BY PURCHASE_NUMBER DESC) AS RN, COUNT(PURCHASE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM PURCHASE) WHERE RN BETWEEN ? AND ?";

	// 2. 한 회원의 모든 주문 목록 출력
	final String SELECTALLONEPERSON = "SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER, TOTAL_COUNT_NUMBER FROM"
			+ " (SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER, ROW_NUMBER() OVER (ORDER BY PURCHASE_NUMBER DESC)"
			+ " AS RN, COUNT(PURCHASE_NUMBER) OVER (PARTITION BY MEMBER_NUMBER) AS TOTAL_COUNT_NUMBER FROM PURCHASE WHERE MEMBER_NUMBER = ?)"
			+ " WHERE RN BETWEEN ? AND ?";

	// 3. 주문 추가
	final String INSERT = "INSERT INTO PURCHASE (PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER)"
			+ " VALUES (?, ?, ?, ?)";

	// 4. 주문 취소
	final String DELETEPURCHASE = "DELETE FROM PURCHASE WHERE PURCHASE_NUMBER = ?";

	// 5. 탈퇴한 회원의 주문 목록 삭제
	final String DELETECANCELMEMBER = "DELETE FROM PURCHASE WHERE MEMBER_NUMBER = ?";

	// 6. 주문 정보 1개 조회
	final String SELECTONE = "SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER FROM PURCHASE WHERE PURCHASE_NUMBER = ?";

	// getAll → R
	public List<PurchaseVO> getAll(PurchaseVO purchaseVO) {

		// 주문 목록 전체 출력(최신순)
		if (purchaseVO.getCondition().equals("SELECTALL")) {
			System.out.println("주문 목록 전체 출력(최신순)");
			Object[] args = { purchaseVO.getPurchaseIndex(), purchaseVO.getPurchaseContentCount() };
			return jdbcTemplate.query(SELECTALL, args, new PurchaseGetAllRowMapper());
		}
		// 회원의 주문 목록 전체 출력
		else if (purchaseVO.getCondition().equals("SELECTALLONEPERSON")) {
			System.out.println("회원의 주문 목록 전체 출력");
			Object[] args = { purchaseVO.getMemberNumber(), purchaseVO.getPurchaseIndex(),
					purchaseVO.getPurchaseContentCount() };
			return jdbcTemplate.query(SELECTALLONEPERSON, args, new PurchaseGetAllOneMemberRowMapper());
		}
		// 조건에 안맞다면 빈 배열 반환
		else {
			return new ArrayList<>();
		}
	}

	// getOne → R
	public PurchaseVO getOne(PurchaseVO purchaseVO) {
		System.out.println("주문 정보 상세 조회");
		Object[] args = { purchaseVO.getPurchaseNumber() };
		List<PurchaseVO> list = jdbcTemplate.query(SELECTONE, args, new PurchaseGetOneRowMapper());

		return getSingleResult(list);
	}
	
	// insert → C
	public boolean insert(PurchaseVO purchaseVO) {
		System.out.println("주문 등록");
		Object[] args = { purchaseVO.getPurchaseNumber(), purchaseVO.getPurchaseTerminalId(),
				purchaseVO.getPurchaseTotalPrice(), purchaseVO.getMemberNumber() };
		// 주문 등록
		int result = jdbcTemplate.update(INSERT, args);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// update → U
	public boolean update(PurchaseVO purchaseVO) {
		return false;
	}

	// delete → D
	public boolean delete(PurchaseVO purchaseVO) {
		int result = 0;
		// 주문 취소
		if (purchaseVO.getCondition().equals("DELETEPURCHASE")) {
			System.out.println("주문 취소");
			Object[] args = { purchaseVO.getPurchaseNumber() };
			result = jdbcTemplate.update(DELETEPURCHASE, args);
		}
		// 탈퇴한 회원의 주문목록 삭제
		else if (purchaseVO.getCondition().equals("DELETECANCELMEMBER")) {
			System.out.println("탈퇴한 회원의 주문목록 삭제");
			Object[] args = { purchaseVO.getMemberNumber() };
			result = jdbcTemplate.update(DELETECANCELMEMBER, args);
		}

		if (result <= 0) {
			return false;
		}
		return true;
	}

	// 반환 메서드
	private PurchaseVO getSingleResult(List<PurchaseVO> list) {
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	// 시퀀스 값 조회(주문번호 생성용)
    public int getNextPurchaseSequence() {
        String sql = "SELECT SEQ_PURCHASE.NEXTVAL FROM dual";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}

// 주문 목록 전체 출력(최신순)
class PurchaseGetAllRowMapper implements RowMapper<PurchaseVO> {

	@Override
	public PurchaseVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		PurchaseVO data = new PurchaseVO();

		data.setPurchaseNumber(rs.getLong("PURCHASE_NUMBER"));
		data.setPurchaseTerminalId(rs.getString("PURCHASE_TERMINAL_ID"));
		data.setPurchaseTotalPrice(rs.getLong("PURCHASE_TOTAL_PRICE"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));

		System.out.println("getAll 주문 목록 전체 데이터 [" + data + "]");
		return data;
	}
}

// 회원의 주문 목록 전체 출력
class PurchaseGetAllOneMemberRowMapper implements RowMapper<PurchaseVO> {

	@Override
	public PurchaseVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		PurchaseVO data = new PurchaseVO();

		data.setPurchaseNumber(rs.getLong("PURCHASE_NUMBER"));
		data.setPurchaseTerminalId(rs.getString("PURCHASE_TERMINAL_ID"));
		data.setPurchaseTotalPrice(rs.getLong("PURCHASE_TOTAL_PRICE"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));

		System.out.println("getAll 회원의 주문 목록 데이터 [" + data + "]");
		return data;
	}
}

// 주문 정보 상세 조회
class PurchaseGetOneRowMapper implements RowMapper<PurchaseVO> {

	@Override
	public PurchaseVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		PurchaseVO data = new PurchaseVO();

		data.setPurchaseNumber(rs.getLong("PURCHASE_NUMBER"));
		data.setPurchaseTerminalId(rs.getString("PURCHASE_TERMINAL_ID"));
		data.setPurchaseTotalPrice(rs.getLong("PURCHASE_TOTAL_PRICE"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));

		System.out.println("getOne 주문 상세 조회 데이터 [" + data + "]");
		return data;
	}

}

//23ai 버전 OFFSET ? ROWS FETCH NEXT ? ROWS ONLY을 사용하였으나 11g에서는 사용 불가

/*
 * // 1. 모든 주문 목록 출력 (최신순) final String SELECTALL =
 * "SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER FROM PURCHASE"
 * + " ORDER BY PURCHASE_NUMBER DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
 * 
 * // 2. 한 회원의 모든 주문 목록 출력 final String SELECTALLONEPERSON =
 * "SELECT PURCHASE.PURCHASE_NUMBER, PURCHASE.PURCHASE_TERMINAL_ID, PURCHASE.PURCHASE_TOTAL_PRICE,"
 * +
 * " PURCHASE.MEMBER_NUMBER, COUNT(PURCHASE.PURCHASE_NUMBER) OVER (PARTITION BY PURCHASE.MEMBER_NUMBER) AS TOTAL_COUNT_NUMBER"
 * +
 * " FROM PURCHASE WHERE PURCHASE.MEMBER_NUMBER = ? ORDER BY PURCHASE.PURCHASE_NUMBER DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // 3. 주문 추가 final String INSERT =
 * "INSERT INTO PURCHASE (PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER)"
 * + " VALUES (SEQ_PURCHASE.NEXTVAL, ?, ?, ?)";
 * 
 * // 4. 주문 취소 final String DELETEPURCHASE =
 * "DELETE FROM PURCHASE WHERE PURCHASE_NUMBER = ?";
 * 
 * // 5. 탈퇴한 회원의 주문 목록 삭제 final String DELETECANCELMEMBER =
 * "DELETE FROM PURCHASE WHERE PURCHASE_NUMBER = ?";
 * 
 * // 6. 주문 정보 1개 조회 final String SELECTONE =
 * "SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER FROM PURCHASE WHERE PURCHASE_NUMBER = ?"
 * ;
 */
