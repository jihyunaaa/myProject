//package zic.honeyComboFactory.biz.productComboComponent.impl;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//import zic.honeyComboFactory.biz.productComboComponentVO.ProductComboComponentVO;
//import zic.honeyComboFactory.common.util.OracleJDBCUtil;
//
//// @Repository("oracleProductComboComponentDAO")
//public class OracleProductComboComponentDAO { // 꿀조합 상품 구성품 기능 - Oracle DB
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
//	// 4. 조합 구성품 출력
//	final String SELECTALLCOMPONENT = "SELECT PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE.PRODUCT_SINGLE_NAME,"
//			+ " PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE, ROUND(PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE *"
//			+ " (1 - NVL(PRODUCT_SINGLE.PRODUCT_SINGLE_DISCOUNT, 0) / 100), 0) AS PRODUCT_SINGLE_DISCOUNTED_PRICE FROM PRODUCT_COMBO_COMPONENT"
//			+ " LEFT JOIN PRODUCT_SINGLE ON PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_ONE OR"
//			+ " PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_TWO OR PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER"
//			+ " = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_THREE WHERE PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_NUMBER = ?";
//
//
//	// R = selectAll()
//	public List<ProductComboComponentVO> getAll(ProductComboComponentVO productComboComponentVO) {
//		List<ProductComboComponentVO> datas = new ArrayList<ProductComboComponentVO>();
//		try {
//			conn = OracleJDBCUtil.connect();
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
//					data.setProductSingleDiscountedPrice(rs.getLong("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
//				}
//				datas.add(data);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			OracleJDBCUtil.disconnect(conn, pstmt);
//		}
//		return datas;
//	}
//
//	// R = selectOne()
//	public ProductComboComponentVO getOne(ProductComboComponentVO productComboComponentVO) {
//		return null;
//	}
//
//	// C
//	public boolean insert(ProductComboComponentVO productComboComponentVO) {
//		try {
//			conn = OracleJDBCUtil.connect();
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
//			OracleJDBCUtil.disconnect(conn, pstmt);
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
//			conn = OracleJDBCUtil.connect();
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
//			OracleJDBCUtil.disconnect(conn, pstmt);
//		}
//		return true;
//	}
//}