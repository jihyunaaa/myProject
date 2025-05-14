package zic.honeyComboFactory.biz.purchase.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import zic.honeyComboFactory.biz.purchaseVO.PurchaseVO;
import zic.honeyComboFactory.common.util.OracleJDBCUtil;

// @Repository("oraclePurchaseDAO")
public class OraclePurchaseDAO { // 주문 기능 - Oracle DB
	private Connection conn;
	private PreparedStatement pstmt;

	// 1. 모든 주문 목록 출력 (최신순)
	final String SELECTALL = "SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER FROM PURCHASE"
			+ " ORDER BY PURCHASE_NUMBER DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";


	// 2. 한 회원의 모든 주문 목록 출력
	final String SELECTALLONEPERSON = "SELECT PURCHASE.PURCHASE_NUMBER, PURCHASE.PURCHASE_TERMINAL_ID, PURCHASE.PURCHASE_TOTAL_PRICE,"
			+ " PURCHASE.MEMBER_NUMBER, COUNT(PURCHASE.PURCHASE_NUMBER) OVER (PARTITION BY PURCHASE.MEMBER_NUMBER) AS TOTAL_COUNT_NUMBER"
			+ " FROM PURCHASE WHERE PURCHASE.MEMBER_NUMBER = ? ORDER BY PURCHASE.PURCHASE_NUMBER DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";


	// 3. 주문 추가
	final String INSERT = "INSERT INTO PURCHASE (PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER)"
			+ " VALUES (SEQ_PURCHASE.NEXTVAL, ?, ?, ?)";


	// 4. 주문 취소
	final String DELETEPURCHASE = "DELETE FROM PURCHASE WHERE PURCHASE_NUMBER = ?";

	// 5. 탈퇴한 회원의 주문 목록 삭제
	final String DELETECANCELMEMBER = "DELETE FROM PURCHASE WHERE PURCHASE_NUMBER = ?";


	// 6. 주문 정보 1개 조회
	final String SELECTONE = "SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER FROM PURCHASE WHERE PURCHASE_NUMBER = ?";


	// R = selectAll()
	public List<PurchaseVO> getAll(PurchaseVO purchaseVO) {
		List<PurchaseVO> datas = new ArrayList<PurchaseVO>();
		System.out.println("[selectAll] 전체 주문 목록 조회 시작");
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[selectAll] DB 연결 완료");

			// 모든 주문목록 (최신순)
			if (purchaseVO.getCondition().equals("SELECTALL")) {
				System.out.println("[selectAll] SELECTALL 조건 실행");
				pstmt = conn.prepareStatement(SELECTALL);
				System.out.println(">> 쿼리 준비 완료: " + pstmt);
				pstmt.setInt(1, purchaseVO.getPurchaseIndex());
				pstmt.setInt(2, purchaseVO.getPurchaseContentCount());
				System.out.println(">> selectall = " + purchaseVO.getPurchaseIndex() + ", "
						+ purchaseVO.getPurchaseContentCount());
			}
			// 한 회원의 모든 주문목록
			else if (purchaseVO.getCondition().equals("SELECTALLONEPERSON")) {
				System.out.println("[selectAll] SELECTALLONEPERSON 조건 실행");
				pstmt = conn.prepareStatement(SELECTALLONEPERSON);
				System.out.println(">> 쿼리 준비 완료: " + pstmt);
				pstmt.setLong(1, purchaseVO.getMemberNumber());
				pstmt.setInt(2, purchaseVO.getPurchaseContentCount());
				pstmt.setInt(3, purchaseVO.getPurchaseIndex());
				System.out.println(">> selectallperson= " + purchaseVO.getMemberNumber());
			}

			ResultSet rs = pstmt.executeQuery();
			System.out.println("[selectAll] 쿼리 실행 완료");
			while (rs.next()) {
				PurchaseVO data = new PurchaseVO();
				// 모든 주문목록 (최신순)
				if (purchaseVO.getCondition().equals("SELECTALL")) {
					data.setPurchaseNumber(rs.getLong("PURCHASE_NUMBER"));
					data.setPurchaseTerminalId(rs.getString("PURCHASE_TERMINAL_ID"));
					data.setPurchaseTotalPrice(rs.getLong("PURCHASE_TOTAL_PRICE"));
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					datas.add(data);
				}
				// 한 회원의 모든 주문목록
				else if (purchaseVO.getCondition().equals("SELECTALLONEPERSON")) {
					data.setPurchaseNumber(rs.getLong("PURCHASE_NUMBER"));
					data.setPurchaseTerminalId(rs.getString("PURCHASE_TERMINAL_ID"));
					data.setPurchaseTotalPrice(rs.getLong("PURCHASE_TOTAL_PRICE"));
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					datas.add(data);
				}
				System.out.println(">> purchasenumber " + data.getPurchaseNumber());
			}
		} catch (Exception e) {
			System.out.println("[selectAll] 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
			System.out.println("[selectAll] DB 연결 해제");
		}
		return datas;
	}

	// R = selectOne()
	public PurchaseVO getOne(PurchaseVO purchaseVO) {
		System.out.println("[selectOne] 주문 단건 조회 시작");
		PurchaseVO data = null;
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[selectOne] DB 연결 완료");

			// 주문 정보 1개 조회
			pstmt = conn.prepareStatement(SELECTONE);
			pstmt.setLong(1, purchaseVO.getPurchaseNumber());
			System.out.println(">> 조회할 PURCHASE_NUMBER: " + purchaseVO.getPurchaseNumber());

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				data = new PurchaseVO();
				data.setPurchaseNumber(rs.getLong("PURCHASE_NUMBER"));
				data.setPurchaseTerminalId(rs.getString("PURCHASE_TERMINAL_ID"));
				data.setPurchaseTotalPrice(rs.getLong("PURCHASE_TOTAL_PRICE"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				System.out.println(">> 주문 정보 조회 성공: " + data.getPurchaseNumber());
			}
		} catch (Exception e) {
			System.err.println("[selectOne] 예외 발생: " + e.getMessage());

			e.printStackTrace();
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
			System.out.println("[selectOne] DB 연결 해제");
		}
		return data;
	}

	// C
	public boolean insert(PurchaseVO purchaseVO) {
		System.out.println("[insert] 주문 등록 시작");
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[insert] DB 연결 완료");

			// 주문 추가
			pstmt = conn.prepareStatement(INSERT);
			pstmt.setString(1, purchaseVO.getPurchaseTerminalId());
			pstmt.setLong(2, purchaseVO.getPurchaseTotalPrice());
			pstmt.setLong(3, purchaseVO.getMemberNumber());
			int rs = pstmt.executeUpdate();
			System.out.println("[insert] 쿼리 실행 완료, 결과: " + rs);

			if (rs <= 0) {
				return false;
			}
		} catch (Exception e) {
			System.out.println("[insert] 예외 발생: " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
			System.out.println("[insert] DB 연결 해제");
		}
		return true;
	}

	// U
	public boolean update(PurchaseVO purchaseVO) {
		return false;
	}

	// D
	public boolean delete(PurchaseVO purchaseVO) {
		System.out.println("[delete] DB 연결 완료");
		try {
			conn = OracleJDBCUtil.connect();
			// 주문 취소
			if (purchaseVO.getCondition().equals("DELETEPURCHASE")) {
				pstmt = conn.prepareStatement(DELETEPURCHASE);
				pstmt.setLong(1, purchaseVO.getPurchaseNumber());
				System.out.println(">> 삭제할 주문번호: " + purchaseVO.getPurchaseNumber());
			}
			// 탈퇴한 회원의 주문목록 삭제
			else if (purchaseVO.getCondition().equals("DELETECANCELMEMBER")) {
				pstmt = conn.prepareStatement(DELETECANCELMEMBER);
				pstmt.setLong(1, purchaseVO.getMemberNumber());
				System.out.println(">> 탈퇴 회원의 주문 전체 삭제 -회원번호: " + purchaseVO.getMemberNumber());
			}
			int rs = pstmt.executeUpdate();
			System.out.println("[delete] 쿼리 실행 완료, 결과: " + rs);

			if (rs <= 0) {
				return false;
			}
		} catch (Exception e) {
			System.out.println("[delete] 예외 발생: " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
			System.out.println("[delete] DB 연결 해제");
		}
		return true;
	}
}