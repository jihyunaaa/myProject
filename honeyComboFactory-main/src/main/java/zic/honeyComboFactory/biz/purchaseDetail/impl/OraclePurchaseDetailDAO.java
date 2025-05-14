package zic.honeyComboFactory.biz.purchaseDetail.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailVO;
import zic.honeyComboFactory.common.util.OracleJDBCUtil;

// @Repository("oraclePurchaseDetailDAO")
public class OraclePurchaseDetailDAO { // 주문상세 기능 - Oracle DB
	private Connection conn;
	private PreparedStatement pstmt;

	// 일치하는 주문번호에 맞춰서 상품 상세 정보 출력
	final String SELECTALL = "SELECT PURCHASE_DETAIL_NUMBER, PURCHASE_NUMBER, MEMBER_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_COMBO_NAME, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE,"
			+ " PURCHASE_PRODUCT_COUNT, PURCHASE_TOTAL_PRICE FROM VIEW_PURCHASE_DETAIL WHERE MEMBER_NUMBER = ? AND PURCHASE_NUMBER = ?";


	// 주문 상세정보 저장
	final String INSERT = "INSERT INTO PURCHASE_DETAIL (PURCHASE_DETAIL_NUMBER, PURCHASE_PRODUCT_COUNT, PRODUCT_SINGLE_NUMBER,"
			+ " PRODUCT_COMBO_NUMBER, PURCHASE_NUMBER) VALUES (SEQ_PURCHASE_DETAIL.NEXTVAL, ?, ?, ?, ?)";


	// R = selectAll()
	public List<PurchaseDetailVO> getAll(PurchaseDetailVO purchaseDetailVO) {
		System.out.println("[selectAll] 주문 상세 조회 시작");
		List<PurchaseDetailVO> datas = new ArrayList<PurchaseDetailVO>();
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[selectAll] DB 연결 완료");

			// 일치하는 주문번호에 맞춰서 상품 상세정보 조회
			pstmt = conn.prepareStatement(SELECTALL);
			pstmt.setLong(1, purchaseDetailVO.getMemberNumber());
			pstmt.setLong(2, purchaseDetailVO.getPurchaseNumber());
			System.out.println("회원번호 확인 [" + purchaseDetailVO.getMemberNumber() + "]");
			System.out.println("주문 상세 번호 입력 [" + purchaseDetailVO.getPurchaseNumber() + "]");
			System.out.println("DTO 디버깅: " + purchaseDetailVO);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("주문 상세 정보 불러오기");

			while (rs.next()) {
				PurchaseDetailVO data = new PurchaseDetailVO();
				data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME")); // 개별 상품 이름
				data.setProductSinglePrice(rs.getLong("PRODUCT_SINGLE_PRICE")); // 개별 상품 원가
				data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT")); // 개별 상품 할인율
				data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE")); // 개별 상품 가격
				data.setProductComboName(rs.getString("PRODUCT_COMBO_NAME")); // 조합 상품 이름
				data.setProductComboPrice(rs.getLong("PRODUCT_COMBO_PRICE")); // 조합 상품 원가
				data.setProductComboDiscount(rs.getInt("PRODUCT_COMBO_DISCOUNT")); // 조합 상품 할인율
				data.setProductComboDiscountedPrice(rs.getInt("PRODUCT_COMBO_DISCOUNTED_PRICE")); // 조합 상품 가격
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
			OracleJDBCUtil.disconnect(conn, pstmt);
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
			conn = OracleJDBCUtil.connect();
			System.out.println("[insert] DB 연결 완료");

			// 주문 상세정보 저장
			pstmt = conn.prepareStatement(INSERT);
			pstmt.setLong(1, purchaseDetailVO.getPurchaseProductCount()); // 수량
			pstmt.setLong(2, purchaseDetailVO.getProductSingleNumber()); // 개별 상품 번호
			pstmt.setLong(3, purchaseDetailVO.getProductComboNumber()); // 조합 상품 번호
			pstmt.setLong(4, purchaseDetailVO.getPurchaseNumber()); // 주문 번호

			System.out.println(">> PURCHASE_DETAIL_NUMBER: " + purchaseDetailVO.getPurchaseDetailNumber());
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
			OracleJDBCUtil.disconnect(conn, pstmt);
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