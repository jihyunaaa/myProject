package zic.honeyComboFactory.biz.purchaseDetail.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailVO;
import zic.honeyComboFactory.common.util.MySQLJDBCUtil;

@Repository("mySQLPurchaseDetailDAO")
public class MySQLPurchaseDetailDAO { // 주문상세 기능 - MySQL DB
	private Connection conn;
	private PreparedStatement pstmt;

	// 일치하는 주문번호에 맞춰서 상품 상세 정보 출력
	final String SELECTALL = "SELECT COALESCE(PRODUCT_SINGLE.PRODUCT_SINGLE_NAME, '') AS PRODUCT_SINGLE_NAME, "
			+ "COALESCE(PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE, 0) AS PRODUCT_SINGLE_PRICE, "
			+ "COALESCE(PRODUCT_COMBO.PRODUCT_COMBO_NAME, '') AS PRODUCT_COMBO_NAME, "
			+ "COALESCE(COMBO_PRICE.PRODUCT_COMBO_PRICE, 0) AS PRODUCT_COMBO_PRICE, "
			+ "PURCHASE_DETAIL.PURCHASE_PRODUCT_COUNT AS PURCHASE_PRODUCT_COUNT, SUM((COALESCE(PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE, 0) "
			+ "+ COALESCE(COMBO_PRICE.PRODUCT_COMBO_PRICE, 0)) * PURCHASE_DETAIL.PURCHASE_PRODUCT_COUNT) OVER() AS PURCHASE_TOTAL_PRICE "
			+ "FROM PURCHASE_DETAIL " + "JOIN PURCHASE ON PURCHASE_DETAIL.PURCHASE_NUMBER = PURCHASE.PURCHASE_NUMBER "
			+ "LEFT JOIN PRODUCT_SINGLE ON PURCHASE_DETAIL.PRODUCT_SINGLE_NUMBER = PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER "
			+ "LEFT JOIN PRODUCT_COMBO ON PURCHASE_DETAIL.PRODUCT_COMBO_NUMBER = PRODUCT_COMBO.PRODUCT_COMBO_NUMBER "
			+ "LEFT JOIN (SELECT PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_NUMBER, COALESCE(S1.PRODUCT_SINGLE_PRICE, 0) "
			+ "+ COALESCE(S2.PRODUCT_SINGLE_PRICE, 0) + COALESCE(S3.PRODUCT_SINGLE_PRICE, 0) AS PRODUCT_COMBO_PRICE "
			+ "FROM PRODUCT_COMBO_COMPONENT "
			+ "LEFT JOIN PRODUCT_SINGLE S1 ON PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_ONE = S1.PRODUCT_SINGLE_NUMBER "
			+ "LEFT JOIN PRODUCT_SINGLE S2 ON PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_TWO = S2.PRODUCT_SINGLE_NUMBER "
			+ "LEFT JOIN PRODUCT_SINGLE S3 ON PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_THREE = S3.PRODUCT_SINGLE_NUMBER) "
			+ "COMBO_PRICE ON PRODUCT_COMBO.PRODUCT_COMBO_NUMBER = COMBO_PRICE.PRODUCT_COMBO_NUMBER "
			+ "WHERE PURCHASE.PURCHASE_NUMBER = ?";

	// 주문 상세정보 저장
	final String INSERT = "INSERT INTO PURCHASE_DETAIL"
			+ " (PURCHASE_DETAIL_NUMBER, PURCHASE_PRODUCT_COUNT, PRODUCT_SINGLE_NUMBER, PRODUCT_COMBO_NUMBER, PURCHASE_NUMBER)"
			+ " SELECT IFNULL((SELECT MAX(PURCHASE_DETAIL_NUMBER) FROM PURCHASE_DETAIL), 0) + 1, ?, ?, ?, ?";

	// R = selectAll()
	public List<PurchaseDetailVO> getAll(PurchaseDetailVO purchaseDetailVO) {
		System.out.println("[selectAll] 주문 상세 조회 시작");
		List<PurchaseDetailVO> datas = new ArrayList<PurchaseDetailVO>();
		try {
			conn = MySQLJDBCUtil.connect();
			System.out.println("[selectAll] DB 연결 완료");

			// 일치하는 주문번호에 맞춰서 상품 상세정보 조회
			pstmt = conn.prepareStatement(SELECTALL);
			pstmt.setLong(1, purchaseDetailVO.getPurchaseNumber());
			System.out.println("주문 상세 번호 입력 [" + purchaseDetailVO.getPurchaseNumber() + "]");
			System.out.println("DTO 디버깅: " + purchaseDetailVO);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("주문 상세 정보 불러오기");

			while (rs.next()) {
				PurchaseDetailVO data = new PurchaseDetailVO();
				data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME")); // 개별 상품 이름
				data.setProductSinglePrice(rs.getLong("PRODUCT_SINGLE_PRICE")); // 개별 상품 가격
				data.setProductComboName(rs.getString("PRODUCT_COMBO_NAME")); // 조합 상품 이름
				data.setProductComboPrice(rs.getLong("PRODUCT_COMBO_PRICE")); // 조합 상품 가격
				data.setPurchaseProductCount(rs.getLong("PURCHASE_PRODUCT_COUNT")); // 수량
				data.setPurchaseTotalPrice(rs.getLong("PURCHASE_TOTAL_PRICE")); // 총 구매 금액
				data.setMemberNumber(purchaseDetailVO.getMemberNumber());
				data.setPurchaseNumber(purchaseDetailVO.getPurchaseNumber());
				System.out.println("배열에 넣은 데이터 [" + data + "]");
				datas.add(data);
			}
		} catch (Exception e) {
			System.out.println("[selectAll] 예외 발생: " + e.getMessage());

			e.printStackTrace();
		} finally {
			MySQLJDBCUtil.disconnect(conn, pstmt);
			System.out.println("[selectAll] DB 연결 해제");

		}
		System.out.println("데이터 [" + datas + "]");
		return datas;
	}

	// R = selectOne()
	public PurchaseDetailVO getOne(PurchaseDetailVO purchaseDetailVO) {
		System.out.println("[selectOne] 주문상세 조회 시작");
		PurchaseDetailVO data = null;
		return data;
	}

	// C
	public boolean insert(PurchaseDetailVO purchaseDetailVO) {
		System.out.println("[insert] 주문 상세 등록 시작");
		try {
			conn = MySQLJDBCUtil.connect();
			System.out.println("[insert] DB 연결 완료");

			// 주문 상세정보 저장
			pstmt = conn.prepareStatement(INSERT);
			pstmt.setLong(1, purchaseDetailVO.getPurchaseProductCount()); // 수량
			pstmt.setLong(2, purchaseDetailVO.getProductSingleNumber()); // 개별 상품 번호
			pstmt.setLong(3, purchaseDetailVO.getProductComboNumber()); // 조합 상품 번호
			pstmt.setLong(4, purchaseDetailVO.getPurchaseNumber()); // 주문 번호

			System.out.println(">> PURCHASE_PRODUCT_COUNT: " + purchaseDetailVO.getPurchaseProductCount());
			System.out.println(">> PRODUCT_SINGLE_NUMBER: " + purchaseDetailVO.getProductSingleNumber());
			System.out.println(">> PRODUCT_COMBO_NUMBER: " + purchaseDetailVO.getProductComboNumber());
			System.out.println(">> PURCHASE_NUMBER: " + purchaseDetailVO.getPurchaseNumber());

			int rs = pstmt.executeUpdate();
			System.out.println("[insert] 쿼리 실행 완료, 결과: " + rs);

			if (rs <= 0) {
				return false;
			}
		} catch (Exception e) {
			System.out.println("[insert] 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			MySQLJDBCUtil.disconnect(conn, pstmt);
			System.out.println("[insert] DB 연결 해제");
		}
		return true;
	}

	// U
	public boolean update(PurchaseDetailVO purchaseDetailVO) {
		System.out.println("[update] 주문상세 수정 시작");
		return false;
	}

	// D
	public boolean delete(PurchaseDetailVO purchaseDetailVO) {
		System.out.println("[delete] 주문상세 삭제 시작");
		return false;
	}
}
