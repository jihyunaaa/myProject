package zic.honeyComboFactory.biz.productCombo.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import zic.honeyComboFactory.biz.productComboVO.ProductComboVO;
import zic.honeyComboFactory.common.util.OracleJDBCUtil;

// @Repository("oracleProductComboDAO")
public class OracleProductComboDAO { // 꿀조합 상품 기능 - Oracle DB
	private Connection conn;
	private PreparedStatement pstmt;

	// 1. 전체 출력 인기순
	final String SELECTALLPOPULAR = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO ORDER BY TOTAL_SALES DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 2. 전체 출력 가격 높은순
	final String SELECTALLPRICEDESC = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES FROM"
			+ " VIEW_PRODUCT_COMBO ORDER BY PRODUCT_COMBO_DISCOUNTED_PRICE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 3. 전체 출력 가격 낮은순
	final String SELECTALLPRICEASC = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO ORDER BY PRODUCT_COMBO_DISCOUNTED_PRICE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 4. 카테고리별 인기순
	final String SELECTALLCATEGORYPOPULAR = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_CATEGORY = ? ORDER BY TOTAL_SALES DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 5. 카테고리별 가격 높은순
	final String SELECTALLCATEGORYDESC = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_CATEGORY = ? ORDER BY PRODUCT_COMBO_DISCOUNTED_PRICE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 6. 카테고리별 가격 낮은순
	final String SELECTALLCATEGORYASC = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_CATEGORY = ? ORDER BY PRODUCT_COMBO_DISCOUNTED_PRICE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 7. CU 콤보 상품 판매순 (STORE = 0)
	final String SELECTALLCUPOPULAR = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_STORE = 0 ORDER BY TOTAL_SALES DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 8. GS25 콤보 상품 판매순 (STORE = 1)
	final String SELECTALLGSPOPULAR = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_STORE = 1 ORDER BY TOTAL_SALES DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 9. MD픽 상품 출력(재고 많은 순)
	final String SELECTALLMD = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_CATEGORY = 'MD' ORDER BY PRODUCT_COMBO_STOCK DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 10. 핫이슈 상품 출력(재고 적은 순)
	final String SELECTALLHOTISSUE = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO ORDER BY PRODUCT_COMBO_STOCK ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 11. 검색 (인기순)
	final String SELECTALLSEARCHPOPULAR = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_NAME LIKE CONCAT('%', ?, '%') ORDER BY TOTAL_SALES DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	
	// 12. 검색 (가격높은순)
	final String SELECTALLSEARCHDESC = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_NAME LIKE CONCAT('%', ?, '%') ORDER BY PRODUCT_COMBO_DISCOUNTED_PRICE DESC"
			+ " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 13. 검색 (가격낮은순)
	final String SELECTALLSEARCHASC = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_NAME LIKE CONCAT('%', ?, '%') ORDER BY PRODUCT_COMBO_DISCOUNTED_PRICE ASC"
			+ " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 14. 상단 광고 MD픽 출력
	final String SELECTONEADVERTISEMENT = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_NUMBER = ?";

	// 15. 콤보 상품 상세 조회
	final String SELECTONE = "SELECT PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STORE, PRODUCT_COMBO_IMAGE,"
			+ " PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_PRICE, PRODUCT_COMBO_DISCOUNT, PRODUCT_COMBO_DISCOUNTED_PRICE, PRODUCT_COMBO_STOCK, TOTAL_SALES"
			+ " FROM VIEW_PRODUCT_COMBO WHERE PRODUCT_COMBO_NUMBER = ?";
	
	// 16. 콤보 상품 삭제
	final String DELETE = "DELETE FROM PRODUCT_COMBO WHERE PRODUCT_COMBO_NUMBER = ?";

	// 17. 콤보 상품 수정
	final String UPDATE = "UPDATE PRODUCT_COMBO SET PRODUCT_COMBO_NAME = ?, PRODUCT_COMBO_IMAGE = ?, PRODUCT_COMBO_INFORMATION = ?, "
			+ "PRODUCT_COMBO_CATEGORY = ?, PRODUCT_COMBO_STORE = ? WHERE PRODUCT_COMBO_NUMBER = ?";

	// 18. 콤보 상품 추가
	/*
	 * final String INSERT =
	 * "INSERT INTO PRODUCT_COMBO (PRODUCT_COMBO_NUMBER, PRODUCT_COMBO_NAME, PRODUCT_COMBO_IMAGE, PRODUCT_COMBO_STORE, "
	 * +
	 * "PRODUCT_COMBO_INFORMATION, PRODUCT_COMBO_CATEGORY, PRODUCT_COMBO_STOCK) SELECT IFNULL(MAX(PRODUCT_COMBO.PRODUCT_COMBO_NUMBER), 0) + 1, "
	 * +
	 * "?, ?, ?, ?, ?, LEAST(COALESCE(PRODUCT_SINGLE.PRODUCT_SINGLE_STOCK, 99999), COALESCE(PRODUCT_SINGLE_2.PRODUCT_SINGLE_STOCK, 99999), "
	 * +
	 * "COALESCE(PRODUCT_SINGLE_3.PRODUCT_SINGLE_STOCK, 99999)) FROM PRODUCT_SINGLE "
	 * +
	 * "JOIN PRODUCT_SINGLE AS PRODUCT_SINGLE_2 ON PRODUCT_SINGLE_2.PRODUCT_SINGLE_NUMBER = ? "
	 * +
	 * "LEFT JOIN PRODUCT_SINGLE AS PRODUCT_SINGLE_3 ON PRODUCT_SINGLE_3.PRODUCT_SINGLE_NUMBER = ? "
	 * + "WHERE PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = ?";
	 */

	// R = selectAll()
	public List<ProductComboVO> getAll(ProductComboVO productComboVO) {
		System.out.println("[selectAll] 콤보 상품 목록 조회 시작");
		List<ProductComboVO> datas = new ArrayList<ProductComboVO>();
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[selectAll] DB 연결 완료");

			// 전체 출력 인기순
			if (productComboVO.getCondition().equals("SELECTALLPOPULAR")) {
				System.out.println("[selectAll] 조건: " + SELECTALLPOPULAR);
				pstmt = conn.prepareStatement(SELECTALLPOPULAR);
				pstmt.setInt(1, productComboVO.getProductComboIndex());
				pstmt.setInt(2, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] 인기순: " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());
			}
			// 전체 출력 가격 높은순
			else if (productComboVO.getCondition().equals("SELECTALLPRICEDESC")) {
				System.out.println("[selectAll] 조건: " + SELECTALLPRICEDESC);
				pstmt = conn.prepareStatement(SELECTALLPRICEDESC);
				pstmt.setInt(1, productComboVO.getProductComboIndex());
				pstmt.setInt(2, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] 인기내림차순: " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());
			}
			// 전체 출력 가격 낮은순
			else if (productComboVO.getCondition().equals("SELECTALLPRICEASC")) {
				System.out.println("[selectAll] 조건: " + SELECTALLPRICEASC);
				pstmt = conn.prepareStatement(SELECTALLPRICEASC);
				pstmt.setInt(1, productComboVO.getProductComboIndex());
				pstmt.setInt(2, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] 가격오름차순: " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());
			}
			// 카테고리별 인기순
			else if (productComboVO.getCondition().equals("SELECTALLCATEGORYPOPULAR")) {
				System.out.println("[selectAll] 조건: " + SELECTALLCATEGORYPOPULAR);
				pstmt = conn.prepareStatement(SELECTALLCATEGORYPOPULAR);
				pstmt.setString(1, productComboVO.getProductComboCategory());
				pstmt.setString(2, productComboVO.getProductComboCategory());
				pstmt.setInt(3, productComboVO.getProductComboIndex());
				pstmt.setInt(4, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] 카테고리인기순: " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());
			}
			// 카테고리별 가격 높은순
			else if (productComboVO.getCondition().equals("SELECTALLCATEGORYDESC")) {
				System.out.println("[selectAll] 조건: " + SELECTALLCATEGORYDESC);
				pstmt = conn.prepareStatement(SELECTALLCATEGORYDESC);
				pstmt.setString(1, productComboVO.getProductComboCategory());
				pstmt.setString(2, productComboVO.getProductComboCategory());
				pstmt.setInt(3, productComboVO.getProductComboIndex());
				pstmt.setInt(4, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] 카테고리내림차순: " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());
			}
			// 카테고리별 가격 낮은순
			else if (productComboVO.getCondition().equals("SELECTALLCATEGORYASC")) {
				System.out.println("[selectAll] 조건: " + SELECTALLCATEGORYASC);
				pstmt = conn.prepareStatement(SELECTALLCATEGORYASC);
				pstmt.setString(1, productComboVO.getProductComboCategory());
				pstmt.setString(2, productComboVO.getProductComboCategory());
				pstmt.setInt(3, productComboVO.getProductComboIndex());
				pstmt.setInt(4, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] 카테고리오름차순: " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());
			}
			// CU 콤보 상품 판매순 (STORE = 0)
			else if (productComboVO.getCondition().equals("SELECTALLCUPOPULAR")) {
				System.out.println("[selectAll] 조건: " + SELECTALLCUPOPULAR);
				pstmt = conn.prepareStatement(SELECTALLCUPOPULAR);
				pstmt.setInt(1, productComboVO.getProductComboIndex());
				pstmt.setInt(2, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] CU인기순: " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());
			}
			// GS25 콤보 상품 판매순 (STORE = 1)
			else if (productComboVO.getCondition().equals("SELECTALLGSPOPULAR")) {
				System.out.println("[selectAll] 조건: " + SELECTALLGSPOPULAR);
				pstmt = conn.prepareStatement(SELECTALLGSPOPULAR);
				pstmt.setInt(1, productComboVO.getProductComboIndex());
				pstmt.setInt(2, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] GS인기순: " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());

			}
			// MD픽
			else if (productComboVO.getCondition().equals("SELECTALLMD")) {
				System.out.println("[selectAll] 조건: " + SELECTALLMD);
				pstmt = conn.prepareStatement(SELECTALLMD);
				pstmt.setInt(1, productComboVO.getProductComboIndex());
				pstmt.setInt(2, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] MD: " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());

			}
			// 핫이슈
			else if (productComboVO.getCondition().equals("SELECTALLHOTISSUE")) {
				System.out.println("[selectAll] 조건: " + SELECTALLHOTISSUE);
				pstmt = conn.prepareStatement(SELECTALLHOTISSUE);
				pstmt.setInt(1, productComboVO.getProductComboIndex());
				pstmt.setInt(2, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] HOTISUUE : " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());

			}
			// 검색(인기순)
			else if (productComboVO.getCondition().equals("SELECTALLSEARCHPOPULAR")) {
				System.out.println("[selectAll] 조건: " + SELECTALLSEARCHPOPULAR);
				pstmt = conn.prepareStatement(SELECTALLSEARCHPOPULAR);
				pstmt.setString(1, productComboVO.getSearchKeyword());
				pstmt.setInt(2, productComboVO.getProductComboIndex());
				pstmt.setInt(3, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] 검색인기순 : " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());

			}
			// 검색(가격높은순)
			else if (productComboVO.getCondition().equals("SELECTALLSEARCHDESC")) {
				System.out.println("[selectAll] 조건: " + SELECTALLSEARCHDESC);
				pstmt = conn.prepareStatement(SELECTALLSEARCHDESC);
				pstmt.setString(1, productComboVO.getSearchKeyword());
				pstmt.setInt(2, productComboVO.getProductComboIndex());
				pstmt.setInt(3, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] 검색최신순 : " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());

			}
			// 검색(가격낮은순)
			else if (productComboVO.getCondition().equals("SELECTALLSEARCHASC")) {
				System.out.println("[selectAll] 조건: " + SELECTALLSEARCHASC);
				pstmt = conn.prepareStatement(SELECTALLSEARCHASC);
				pstmt.setString(1, productComboVO.getSearchKeyword());
				pstmt.setInt(2, productComboVO.getProductComboIndex());
				pstmt.setInt(3, productComboVO.getProductComboContentCount());
				System.out.println("[selectAll] 검색오름차순 : " + productComboVO.getProductComboIndex() + ", "
						+ productComboVO.getProductComboContentCount());

			}
			ResultSet rs = pstmt.executeQuery();
			System.out.println("[selectAll] 쿼리 실행 완료");
			while (rs.next()) {
				ProductComboVO data = new ProductComboVO();
				System.out.println("[selectAll] 결과 추출 시작");
				// 전체 출력 인기순
				if (productComboVO.getCondition().equals("SELECTALLPOPULAR")
						|| productComboVO.getCondition().equals("SELECTALLPRICEDESC")
						|| productComboVO.getCondition().equals("SELECTALLPRICEASC")
						|| productComboVO.getCondition().equals("SELECTALLCATEGORYPOPULAR")
						|| productComboVO.getCondition().equals("SELECTALLCATEGORYDESC")
						|| productComboVO.getCondition().equals("SELECTALLCATEGORYASC")
						|| productComboVO.getCondition().equals("SELECTALLCUPOPULAR")
						|| productComboVO.getCondition().equals("SELECTALLGSPOPULAR")
						|| productComboVO.getCondition().equals("SELECTALLMD")
						|| productComboVO.getCondition().equals("SELECTALLHOTISSUE")) {

					data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
					data.setProductComboName(rs.getString("PRODUCT_COMBO_NAME"));
					data.setProductComboStore(rs.getInt("PRODUCT_COMBO_STORE"));
					data.setProductComboCategory(rs.getString("PRODUCT_COMBO_CATEGORY"));
					data.setProductComboPrice(rs.getInt("PRODUCT_COMBO_PRICE"));
					data.setProductComboImage(rs.getString("PRODUCT_COMBO_IMAGE"));
					data.setProductComboStock(rs.getInt("PRODUCT_COMBO_STOCK"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					
					// 할인율
					data.setProductComboDiscount(rs.getInt("PRODUCT_COMBO_DISCOUNT"));
					// 할인된 가격
					data.setProductComboDiscountedPrice(rs.getInt("PRODUCT_COMBO_DISCOUNTED_PRICE"));
				}
				// 검색 (인기순)
				else if (productComboVO.getCondition().equals("SELECTALLSEARCHPOPULAR")
						|| productComboVO.getCondition().equals("SELECTALLSEARCHDESC")
						|| productComboVO.getCondition().equals("SELECTALLSEARCHASC")) {

					data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
					data.setProductComboName(rs.getString("PRODUCT_COMBO_NAME"));
					data.setProductComboStore(rs.getInt("PRODUCT_COMBO_STORE"));
					data.setProductComboCategory(rs.getString("PRODUCT_COMBO_CATEGORY"));
					data.setProductComboPrice(rs.getInt("PRODUCT_COMBO_PRICE"));
					data.setProductComboImage(rs.getString("PRODUCT_COMBO_IMAGE"));
					data.setProductComboStock(rs.getInt("PRODUCT_COMBO_STOCK"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					data.setProductComboInformation(rs.getString("PRODUCT_COMBO_INFORMATION"));

					// 할인율
					data.setProductComboDiscount(rs.getInt("PRODUCT_COMBO_DISCOUNT"));
					// 할인된 가격
					data.setProductComboDiscountedPrice(rs.getInt("PRODUCT_COMBO_DISCOUNTED_PRICE"));
				}
				datas.add(data);
				System.out.println("M 로그 : [" + datas + "]");
			}
		} catch (Exception e) {
			System.out.println("[selectOne] 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
			System.out.println("[selectOne] DB 연결 해제");

		}
		return datas;
	}

	// R = selectOne()
	public ProductComboVO getOne(ProductComboVO productComboVO) {
		System.out.println("[selectOne] 콤보 상품 상세 조회 시작");
		ProductComboVO data = null;
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[selectOne] DB 연결 완료");
			// 콤보 상품 상세 조회
			if (productComboVO.getCondition().equals("SELECTONE")) {
				pstmt = conn.prepareStatement(SELECTONE);
				pstmt.setLong(1, productComboVO.getProductComboNumber());
				System.out.println("[selectOne] 조회번호: " + productComboVO.getProductComboNumber());
			}
			// 상단 광고 MD픽
			else if (productComboVO.getCondition().equals("SELECTONEADVERTISEMENT")) {
				pstmt = conn.prepareStatement(SELECTONEADVERTISEMENT);
				pstmt.setInt(1, productComboVO.getProductComboADNumber());
				System.out.println("[selectOne] 광고 번호: " + productComboVO.getProductComboADNumber());
			}
			ResultSet rs = pstmt.executeQuery();
			System.out.println("[selectOne] 쿼리 실행 완료");
			if (rs.next()) {
				System.out.println("[selectOne] 데이터 조회 성공");
				data = new ProductComboVO();
				data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
				data.setProductComboName(rs.getString("PRODUCT_COMBO_NAME"));
				data.setProductComboCategory(rs.getString("PRODUCT_COMBO_CATEGORY"));
				data.setProductComboStore(rs.getInt("PRODUCT_COMBO_STORE"));
				data.setProductComboPrice(rs.getInt("PRODUCT_COMBO_PRICE"));
				data.setProductComboStock(rs.getInt("PRODUCT_COMBO_STOCK"));
				data.setProductComboImage(rs.getString("PRODUCT_COMBO_IMAGE"));
				data.setProductComboInformation(rs.getString("PRODUCT_COMBO_INFORMATION"));

				// 할인율
				data.setProductComboDiscount(rs.getInt("PRODUCT_COMBO_DISCOUNT"));
				// 할인된 가격
				data.setProductComboDiscountedPrice(rs.getInt("PRODUCT_COMBO_DISCOUNTED_PRICE"));
				System.out.println("[selectOne] DTO 구성 완료: " + data.toString());
			}
		} catch (Exception e) {
			System.out.println("[selectOne] 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
			System.out.println("[selectOne] DB 연결 해제");
		}
		return data;
	}

	// C
	public boolean insert(ProductComboVO productComboVO) {
		return false;
	}

	// U
	public boolean update(ProductComboVO productComboVO) {
		System.out.println("[update] 콤보 상품 수정 시작");
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[update] DB 연결 완료");
			// 콤보 상품 수정
			pstmt = conn.prepareStatement(UPDATE);
			pstmt.setString(1, productComboVO.getProductComboName());
			pstmt.setString(2, productComboVO.getProductComboImage());
			pstmt.setString(3, productComboVO.getProductComboInformation());
			pstmt.setString(4, productComboVO.getProductComboCategory());
			pstmt.setInt(5, productComboVO.getProductComboStore());
			pstmt.setLong(6, productComboVO.getProductComboNumber());
			System.out.println("[update] 상품수정 완료: " + productComboVO.toString());

			int rs = pstmt.executeUpdate();
			System.out.println("[update] 실행 결과 행 수: " + rs);

			if (rs <= 0) {
				System.out.println("[update] 수정 실패");
				return false;
			}
		} catch (Exception e) {
			System.out.println("[update] 예외 발생: " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
			System.out.println("[update] DB 연결 해제");
		}
		return true;
	}

	// D
	public boolean delete(ProductComboVO productComboVO) {
		System.out.println("[delete] 콤보 상품 삭제 시작");
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[delete] DB 연결 완료");
			// 콤보 상품 삭제
			pstmt = conn.prepareStatement(DELETE);
			pstmt.setLong(1, productComboVO.getProductComboNumber());
			System.out.println("[delete] 삭제: " + productComboVO.getProductComboNumber());

			int rs = pstmt.executeUpdate();
			if (rs <= 0) {
				System.out.println("[delete] 삭제 실패");
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