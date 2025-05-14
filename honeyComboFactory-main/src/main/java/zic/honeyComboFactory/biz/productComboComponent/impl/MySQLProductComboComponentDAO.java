//package zic.honeyComboFactory.biz.productComboComponent.impl;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.stereotype.Repository;
//
//import zic.honeyComboFactory.biz.productComboComponentVO.ProductComboComponentVO;
//import zic.honeyComboFactory.common.util.MySQLJDBCUtil;
//
//@Repository("mySQLProductComboComponentDAO")
//public class MySQLProductComboComponentDAO { // 꿀조합 상품 구성품 기능 - MySQL DB
//	private Connection conn;
//	private PreparedStatement pstmt;
//
//	// 1. 꿀조합 구성품 전체 조회
//	final String SELECTALL = "SELECT PRODUCT_COMBO_COMPONENT_NUMBER, PRODUCT_COMBO_COMPONENT_ONE, PRODUCT_COMBO_COMPONENT_TWO, "
//			+ "PRODUCT_COMBO_COMPONENT_THREE, PRODUCT_COMBO_NUMBER, PRODUCT_SINGLE_NUMBER, "
//			+ "(SELECT COUNT(PRODUCT_COMBO_COMPONENT_NUMBER) FROM PRODUCT_COMBO_COMPONENT) AS TOTAL_COUNT_NUMBER "
//			+ "FROM PRODUCT_COMBO_COMPONENT LIMIT ?, ?";
//
//	// 2. 꿀조합 구성품 추가
//	final String INSERT = "INSERT INTO PRODUCT_COMBO_COMPONENT (PRODUCT_COMBO_COMPONENT_NUMBER, PRODUCT_COMBO_COMPONENT_ONE, "
//			+ "PRODUCT_COMBO_COMPONENT_TWO, PRODUCT_COMBO_COMPONENT_THREE, PRODUCT_COMBO_NUMBER, PRODUCT_SINGLE_NUMBER) "
//			+ "VALUES (?, ?, ?, ?, ?, ?)";
//
//	// 3. 꿀조합 구성품 삭제
//	final String DELETE = "DELETE FROM PRODUCT_COMBO_COMPONENT WHERE PRODUCT_COMBO_COMPONENT_NUMBER = ?";
//
//	// 4. 각 조합 구성품 정보 출력
//	final String SELECTALLCOMPONENT = "SELECT PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE.PRODUCT_SINGLE_NAME, "
//			+ "PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE FROM PRODUCT_COMBO_COMPONENT "
//			+ "LEFT JOIN PRODUCT_SINGLE ON PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_ONE "
//			+ "OR PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_TWO "
//			+ "OR PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_THREE "
//			+ "WHERE PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_NUMBER = ?";
//
//	// 5. 조합 구성품 번호 출력
//	final String SELECTONECOMPONENTNUMBER = "SELECT PRODUCT_COMBO_COMPONENT_ONE, PRODUCT_COMBO_COMPONENT_TWO, PRODUCT_COMBO_COMPONENT_THREE"
//			+ " FROM PRODUCT_COMBO_COMPONENT WHERE PRODUCT_COMBO_NUMBER = ?";
//
//	// R = selectAll()
//	public List<ProductComboComponentVO> getAll(ProductComboComponentVO productComboComponentVO) {
//		List<ProductComboComponentVO> datas = new ArrayList<ProductComboComponentVO>();
//		try {
//			conn = MySQLJDBCUtil.connect();
//			// 꿀조합 구성품 전체 조회
//			if (productComboComponentVO.getCondition().equals("SELECTALL")) {
//				productComboComponentVO.setCondition("SELECTALL");
//				System.out.println("[selectAll] 조건: " + SELECTALL);
//				pstmt = conn.prepareStatement(SELECTALL);
//				System.out.println(">> 쿼리 준비 완료: " + pstmt);
//				pstmt.setInt(1, productComboComponentVO.getProductComboComponentIndex());
//				pstmt.setInt(2, productComboComponentVO.getProductComboComponentContentCount());
//			}
//			// 조합 구성품 출력
//			else if (productComboComponentVO.getCondition().equals("SELECTALLCOMPONENT")) {
//				productComboComponentVO.setCondition("SELECTALLCOMPONENT");
//				System.out.println("[selectAll] 조건: " + SELECTALLCOMPONENT);
//				pstmt = conn.prepareStatement(SELECTALLCOMPONENT);
//				System.out.println(">> 쿼리 준비 완료: " + pstmt);
//				pstmt.setLong(1, productComboComponentVO.getProductComboNumber());
//			} else {
//				System.out.println("잘못된 condition: " + productComboComponentVO.getCondition());
//			}
//			ResultSet rs = pstmt.executeQuery();
//			System.out.println("[selectAll] 쿼리 실행 완료");
//			while (rs.next()) {
//				ProductComboComponentVO data = new ProductComboComponentVO();
//				// 꿀조합 구성품 전체 조회
//				if (productComboComponentVO.getCondition().equals("SELECTALL")) {
//					data.setProductComboComponentNumber(rs.getLong("PRODUCT_COMBO_COMPONENT_NUMBER"));
//					data.setProductComboComponentOne(rs.getLong("PRODUCT_COMBO_COMPONENT_ONE"));
//					data.setProductComboComponentTwo(rs.getLong("PRODUCT_COMBO_COMPONENT_TWO"));
//					data.setProductComboComponentThree(rs.getLong("PRODUCT_COMBO_COMPONENT_THREE"));
//					data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
//					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
//					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
//				}
//				// 조합 구성품 출력
//				else if (productComboComponentVO.getCondition().equals("SELECTALLCOMPONENT")) {
//					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
//					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
//					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
//					data.setProductSinglePrice(rs.getLong("PRODUCT_SINGLE_PRICE"));
//				}
//				datas.add(data);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			MySQLJDBCUtil.disconnect(conn, pstmt);
//		}
//		return datas;
//	}
//
//	// R = selectOne()
//	public ProductComboComponentVO getOne(ProductComboComponentVO productComboComponentVO) {
//		ProductComboComponentVO data = null; // 기본 null로 설정
//		// scope문제로 위에 선언 및 try~catch문으로 인한 변수 초기화
//		System.out.println(
//				"ProductComboComponentDAO 로그-받은 selectOne 조건 : [" + productComboComponentVO.getCondition() + "]");
//		// 꿀조합 상품 구성품 번호
//		if (productComboComponentVO.getCondition().equals("SELECTONECOMPONENTNUMBER")) {
//			try { // 에러 가능성 있는 코드
//					// 드라이버 로드 및 연결
//				conn = MySQLJDBCUtil.connect();
//				// 데이터 read
//				// 상품 상세정보
//				pstmt = conn.prepareStatement(SELECTONECOMPONENTNUMBER); // 구문 저장
//				pstmt.setLong(1, productComboComponentVO.getProductComboNumber()); // 파서 값 저장
//
//				// 결과 rs에 저장
//				ResultSet rs = pstmt.executeQuery();
//
//				while (rs.next()) { // 다음 데이터가 있다면
//					data = new ProductComboComponentVO(); // 객체화
//					// 꿀조합 상푸 구성품 번호
//					if (productComboComponentVO.getCondition().equals("SELECTONECOMPONENTNUMBER")) {
//						data.setProductComboComponentOne(rs.getLong("PRODUCT_COMBO_COMPONENT_ONE")); // 값들 저장
//						data.setProductComboComponentTwo(rs.getLong("PRODUCT_COMBO_COMPONENT_TWO"));
//						data.setProductComboComponentThree(rs.getLong("PRODUCT_COMBO_COMPONENT_THREE"));
//					}
//				}
//			} catch (Exception e) { // 에러가 났다면
//				e.printStackTrace(); // 에러 내용 출력
//			} finally { // 반드시 실행할 코드
//				// DB 연결 해제
//				MySQLJDBCUtil.disconnect(conn, pstmt);
//			}
//		}
//		System.out.println("ProductComboComponentDAO 로그-반환 selectOne : [" + data + "]");
//		return data; // 데이터 반환
//	}
//
//	// C
//	public boolean insert(ProductComboComponentVO productComboComponentVO) {
//		try {
//			conn = MySQLJDBCUtil.connect();
//			// 꿀조합 구성품 추가
//			pstmt = conn.prepareStatement(INSERT);
//			pstmt.setLong(1, productComboComponentVO.getProductComboComponentOne());
//			pstmt.setLong(2, productComboComponentVO.getProductComboComponentTwo());
//			pstmt.setLong(3, productComboComponentVO.getProductComboComponentThree());
//			pstmt.setLong(4, productComboComponentVO.getProductComboNumber());
//			pstmt.setLong(5, productComboComponentVO.getProductSingleNumber());
//			int rs = pstmt.executeUpdate();
//			if (rs <= 0) {
//				return false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//			MySQLJDBCUtil.disconnect(conn, pstmt);
//		}
//		return true;
//	}
//
//	// U
//	public boolean update(ProductComboComponentVO productComboComponentVO) {
//		return false;
//	}
//
//	// D
//	public boolean delete(ProductComboComponentVO productComboComponentVO) {
//		try {
//			conn = MySQLJDBCUtil.connect();
//			// 꿀조합 구성품 삭제
//			pstmt = conn.prepareStatement(DELETE);
//			pstmt.setLong(1, productComboComponentVO.getProductComboComponentNumber());
//			int rs = pstmt.executeUpdate();
//			if (rs <= 0) {
//				return false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//			MySQLJDBCUtil.disconnect(conn, pstmt);
//		}
//		return true;
//	}
//}
