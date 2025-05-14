package zic.honeyComboFactory.biz.productSingle.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;
import zic.honeyComboFactory.common.util.OracleJDBCUtil;

public class OracleProductSingleDAO { // 개별 상품 기능 - Oracle DB
	private Connection conn;
	private PreparedStatement pstmt;

	// 1. 상품 카테고리(핫이슈, +1 증정상품)
	final String SELECTALLCATEGORY = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
			+ " WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 2. 상품이름 검색(인기순)
	final String SELECTALLSEARCHPOPULAR = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
			+ " WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_NAME LIKE '%' || ? || '%' OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 3. 상품 전체+인기순
	final String SELECTALLPOPULAR = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER,"
			+ " COUNT(PURCHASE_DETAIL.PURCHASE_DETAIL_NUMBER) AS PURCHASE_COUNT FROM VIEW_PRODUCT_SINGLE LEFT JOIN PURCHASE_DETAIL"
			+ " ON PRODUCT_SINGLE_NUMBER = PURCHASE_DETAIL.PRODUCT_SINGLE_NUMBER WHERE PRODUCT_SINGLE_STORE = ? GROUP BY PRODUCT_SINGLE_NUMBER,"
			+ " PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_DISCOUNTED_PRICE"
			+ " ORDER BY PURCHASE_COUNT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 4. 상품 전체+가격 높은순
	final String SELECTALLPRICEDESC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? ORDER BY PRODUCT_SINGLE_DISCOUNTED_PRICE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 5. 상품 전체+가격 낮은순
	final String SELECTALLPRICEASC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? ORDER BY PRODUCT_SINGLE_DISCOUNTED_PRICE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 6. 상품 카테고리+인기순
	final String SELECTALLCATEGORYPOPULAR = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER,"
			+ " COUNT(PURCHASE_DETAIL.PURCHASE_DETAIL_NUMBER) AS PURCHASE_COUNT FROM VIEW_PRODUCT_SINGLE LEFT JOIN PURCHASE_DETAIL"
			+ " ON PRODUCT_SINGLE_NUMBER = PURCHASE_DETAIL.PRODUCT_SINGLE_NUMBER WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ?"
			+ " GROUP BY PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, PRODUCT_SINGLE_IMAGE,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE ORDER BY PURCHASE_COUNT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 7. 상품 카테고리+가격 높은순
	final String SELECTALLCATEGORYPRICEDESC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE,"
			+ " PRODUCT_SINGLE_DISCOUNT, PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER()"
			+ " AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ?"
			+ " ORDER BY PRODUCT_SINGLE_DISCOUNTED_PRICE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 8. 상품 카테고리+가격 낮은순
	final String SELECTALLCATEGORYPRICEASC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
			+ " WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ? ORDER BY PRODUCT_SINGLE_DISCOUNTED_PRICE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 9. 상품이름 검색(가격 높은 순)
	final String SELECTALLSEARCHPRICEDESC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
			+ " WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_NAME LIKE '%' || ? || '%' ORDER BY PRODUCT_SINGLE_DISCOUNTED_PRICE DESC"
			+ " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 10. 상품이름 검색(가격 낮은 순)
	final String SELECTALLSEARCHPRICEASC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_NAME LIKE '%' || ? || '%' ORDER BY"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 11. 개별상품 재고순 정렬
	final String SELECTALLSTOCKDESC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_STOCK, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE, PRODUCT_SINGLE_CATEGORY,"
			+ " PRODUCT_SINGLE_INFORMATION, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
			+ " ORDER BY PRODUCT_SINGLE_STOCK DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// 12. 상품 상세정보
	final String SELECTONE = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_PRICE,"
			+ " PRODUCT_SINGLE_DISCOUNT, PRODUCT_SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, PRODUCT_SINGLE_INFORMATION"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NUMBER = ?";

	// 13. 개별상품 추가
	final String INSERT = "INSERT INTO PRODUCT_SINGLE (PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_STOCK, "
			+ "PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE, PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_INFORMATION, PRODUCT_SINGLE_DISCOUNT) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	// R = selectAll()
	public List<ProductSingleVO> getAll(ProductSingleVO productSingleVO) {
		List<ProductSingleVO> datas = new ArrayList<ProductSingleVO>(); // 배열 객체화
		// scope문제로 위에 선언 및 try~catch문으로 인한 변수 초기화
		try { // 에러 가능성 있는 코드
				// 드라이버 로드 및 연결
			conn = OracleJDBCUtil.connect();
			// 데이터 read
			System.out.println("ProductSingleDAO 로그-받은 selectAll 조건 : [" + productSingleVO.getCondition() + "]");
			// 1. 상품 카테고리(핫이슈, +1 증정상품)
			if (productSingleVO.getCondition().equals("SELECTALLCATEGORY")) {
				pstmt = conn.prepareStatement(SELECTALLCATEGORY); // 구문 저장
				pstmt.setString(1, productSingleVO.getProductSingleStore()); // 파서 값 저장
				pstmt.setString(2, productSingleVO.getProductSingleCategory());
				pstmt.setInt(3, productSingleVO.getLimitNumber());
				pstmt.setInt(4, productSingleVO.getStartIndex());
			} // 개별상품 번호, 개별상품 이름, 개별상품 가격, 개별상품 사진, 총개수
				// 2. 상품이름 검색(인기순)
			else if (productSingleVO.getCondition().equals("SELECTALLSEARCHPOPULAR")) {
				pstmt = conn.prepareStatement(SELECTALLSEARCHPOPULAR); // 구문 저장
				pstmt.setString(1, productSingleVO.getProductSingleStore());
				pstmt.setString(2, productSingleVO.getSearchKeyword());
				pstmt.setInt(3, productSingleVO.getLimitNumber());
				pstmt.setLong(4, productSingleVO.getStartIndex());
				// pstmt.setString(1, productSingleDTO.getProductSingleStore()); // 파서 값 저장
			}
			// 3. 상품 전체+인기순
			else if (productSingleVO.getCondition().equals("SELECTALLPOPULAR")) {
				pstmt = conn.prepareStatement(SELECTALLPOPULAR); // 구문 저장
				pstmt.setString(1, productSingleVO.getProductSingleStore()); // 파서 값 저장
				pstmt.setInt(2, productSingleVO.getLimitNumber());
				pstmt.setLong(3, productSingleVO.getStartIndex());
			}
			// 4. 상품 전체+가격 높은순
			else if (productSingleVO.getCondition().equals("SELECTALLPRICEDESC")) {
				pstmt = conn.prepareStatement(SELECTALLPRICEDESC); // 구문 저장
				pstmt.setString(1, productSingleVO.getProductSingleStore()); // 파서 값 저장
				pstmt.setInt(2, productSingleVO.getLimitNumber());
				pstmt.setLong(3, productSingleVO.getStartIndex());
			}
			// 5. 상품 전체+가격 낮은순
			else if (productSingleVO.getCondition().equals("SELECTALLPRICEASC")) {
				pstmt = conn.prepareStatement(SELECTALLPRICEASC); // 구문 저장
				pstmt.setString(1, productSingleVO.getProductSingleStore()); // 파서 값 저장
				pstmt.setInt(2, productSingleVO.getLimitNumber());
				pstmt.setLong(3, productSingleVO.getStartIndex());
			}
			// 6. 상품 카테고리+인기순
			else if (productSingleVO.getCondition().equals("SELECTALLCATEGORYPOPULAR")) {
				pstmt = conn.prepareStatement(SELECTALLCATEGORYPOPULAR); // 구문 저장
				pstmt.setString(1, productSingleVO.getProductSingleStore()); // 파서 값 저장
				pstmt.setString(2, productSingleVO.getProductSingleCategory());
				pstmt.setInt(3, productSingleVO.getLimitNumber());
				pstmt.setLong(4, productSingleVO.getStartIndex());
			}
			// 7. 상품 카테고리+가격 높은순
			else if (productSingleVO.getCondition().equals("SELECTALLCATEGORYPRICEDESC")) {
				pstmt = conn.prepareStatement(SELECTALLCATEGORYPRICEDESC); // 구문 저장
				pstmt.setString(1, productSingleVO.getProductSingleStore()); // 파서 값 저장
				pstmt.setString(2, productSingleVO.getProductSingleCategory());
				pstmt.setInt(3, productSingleVO.getLimitNumber());
				pstmt.setLong(4, productSingleVO.getStartIndex());
			}
			// 8. 상품 카테고리+가격 낮은순
			else if (productSingleVO.getCondition().equals("SELECTALLCATEGORYPRICEASC")) {
				pstmt = conn.prepareStatement(SELECTALLCATEGORYPRICEASC); // 구문 저장
				pstmt.setString(1, productSingleVO.getProductSingleStore()); // 파서 값 저장
				pstmt.setString(2, productSingleVO.getProductSingleCategory());
				pstmt.setInt(3, productSingleVO.getLimitNumber());
				pstmt.setLong(4, productSingleVO.getStartIndex());
			}
			// 9. 상품이름 검색(가격 높은 순)
			else if (productSingleVO.getCondition().equals("SELECTALLSEARCHPRICEDESC")) {
				pstmt = conn.prepareStatement(SELECTALLSEARCHPRICEDESC); // 구문 저장
				pstmt.setString(1, productSingleVO.getProductSingleStore());
				pstmt.setString(2, productSingleVO.getSearchKeyword());
				pstmt.setInt(3, productSingleVO.getLimitNumber());
				pstmt.setLong(4, productSingleVO.getStartIndex());
			}
			// 10. 상품이름 검색(가격 낮은 순)
			else if (productSingleVO.getCondition().equals("SELECTALLSEARCHPRICEASC")) {
				pstmt = conn.prepareStatement(SELECTALLSEARCHPRICEASC); // 구문 저장
				pstmt.setString(1, productSingleVO.getProductSingleStore());
				pstmt.setString(2, productSingleVO.getSearchKeyword());
				pstmt.setInt(3, productSingleVO.getLimitNumber());
				pstmt.setLong(4, productSingleVO.getStartIndex());
			}
			// 11. 개별상품 재고순 정렬
			else if (productSingleVO.getCondition().equals("SELECTALLSTOCKDESC")) {
				pstmt = conn.prepareStatement(SELECTALLSTOCKDESC); // 구문 저장
				pstmt.setInt(1, productSingleVO.getLimitNumber());
				pstmt.setLong(2, productSingleVO.getStartIndex());
			}
			// 결과 rs에 저장
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) { // 다음 데이터가 있다면
				ProductSingleVO data = new ProductSingleVO(); // 객체화
				// 상품 카테고리(핫이슈, +1 증정상품)
				if (productSingleVO.getCondition().equals("SELECTALLCATEGORY")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
				// 상품이름 검색(인기순)
				else if (productSingleVO.getCondition().equals("SELECTALLSEARCHPOPULAR")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
				// 상품 전체+인기순
				else if (productSingleVO.getCondition().equals("SELECTALLPOPULAR")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
				// 상품 전체+가격 높은순
				else if (productSingleVO.getCondition().equals("SELECTALLPRICEDESC")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
				// 상품 전체+가격 낮은순
				else if (productSingleVO.getCondition().equals("SELECTALLPRICEASC")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
				// 상품 카테고리+인기순
				else if (productSingleVO.getCondition().equals("SELECTALLCATEGORYPOPULAR")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
				// 상품 카테고리+가격 높은순
				else if (productSingleVO.getCondition().equals("SELECTALLCATEGORYPRICEDESC")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
				// 상품 카테고리+가격 낮은순
				else if (productSingleVO.getCondition().equals("SELECTALLCATEGORYPRICEASC")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
				// 상품이름 검색(가격 높은 순)
				else if (productSingleVO.getCondition().equals("SELECTALLSEARCHPRICEDESC")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
				// 상품이름 검색(가격 낮은 순)
				else if (productSingleVO.getCondition().equals("SELECTALLSEARCHPRICEASC")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
				// 개별상품 재고순 정렬
				else if (productSingleVO.getCondition().equals("SELECTALLSTOCKDESC")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}

				else {
					System.out.println("M 로그 : 잘못된 컨디션 입력");
				}
				System.out.println("ProductSingleDAO 로그-반환 selectALL : [" + productSingleVO.getCondition() + "]");
				datas.add(data); // 배열에 추가
				System.out.println(datas);
			}
		} catch (Exception e) { // 에러가 났다면
			e.printStackTrace(); // 에러 내용 출력
		} finally { // 반드시 실행할 코드
			// DB 연결 해제
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
		System.out.println("ProductSingleDAO 로그-반환 selectALL : [" + datas + "]");
		return datas; // 배열 반환
	}

	// R = selectOne()
	public ProductSingleVO getOne(ProductSingleVO productSingleVO) {
		ProductSingleVO data = null; // 기본 null로 설정
		// scope문제로 위에 선언 및 try~catch문으로 인한 변수 초기화
		System.out.println("ProductSingleDAO 로그-받은 selectOne 조건 : [" + productSingleVO.getCondition() + "]");
		// CU 상품 상세정보
		if (productSingleVO.getCondition().equals("SELECTONE")) {
			try { // 에러 가능성 있는 코드
					// 드라이버 로드 및 연결
				conn = OracleJDBCUtil.connect();
				// 데이터 read
				// 상품 상세정보
				pstmt = conn.prepareStatement(SELECTONE); // 구문 저장
				pstmt.setLong(1, productSingleVO.getProductSingleNumber()); // 파서 값 저장
				System.out.println("SELECT_9 실행할 개별 상품 번호 [" + productSingleVO.getProductSingleNumber() + "]");

				// 결과 rs에 저장
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) { // 다음 데이터가 있다면
					data = new ProductSingleVO(); // 객체화
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER")); // 값들 저장
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSingleCategory(rs.getString("PRODUCT_SINGLE_CATEGORY"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setProductSingleStock(rs.getInt("PRODUCT_SINGLE_STOCK"));
					data.setProductSingleInformation(rs.getString("PRODUCT_SINGLE_INFORMATION"));
					// 할인율
					data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
					// 할인된 가격
					data.setProductSingleDiscountedPrice(rs.getInt("PRODUCT_SINGLE_DISCOUNTED_PRICE"));
				}
			} catch (Exception e) { // 에러가 났다면
				e.printStackTrace(); // 에러 내용 출력
			} finally { // 반드시 실행할 코드
				// DB 연결 해제
				OracleJDBCUtil.disconnect(conn, pstmt);
			}
		}
		System.out.println("ProductSingleDAO 로그-반환 selectOne : [" + data + "]");
		return data; // 데이터 반환
	}

	// C
	public boolean insert(ProductSingleVO productSingleVO) {
		if (existsByName(productSingleVO.getProductSingleName())) {// 중복 상품 확인 메서드
			System.out.println("[DAO/insert] 중복 상품 등록 생략: " + productSingleVO.getProductSingleName());
			return false;
		}
		if (productSingleVO.getProductSingleNumber() == 0) {// 상품번호 자동 생성
			productSingleVO
					.setProductSingleNumber(getNextProductSingleNumberByBrand(productSingleVO.getProductSingleStore()));
		}
		try {
			conn = OracleJDBCUtil.connect();
			// 개별상품 추가
			pstmt = conn.prepareStatement(INSERT);
			pstmt.setLong(1, productSingleVO.getProductSingleNumber());
			pstmt.setString(2, productSingleVO.getProductSingleName());
			pstmt.setInt(3, productSingleVO.getProductSinglePrice());
			pstmt.setInt(4, productSingleVO.getProductSingleStock());
			pstmt.setString(5, productSingleVO.getProductSingleImage());
			pstmt.setString(6, productSingleVO.getProductSingleStore());
			pstmt.setString(7, productSingleVO.getProductSingleCategory());

			if (productSingleVO.getProductSingleInformation() != null
					&& !productSingleVO.getProductSingleInformation().isEmpty()) {
				pstmt.setString(8, productSingleVO.getProductSingleInformation());
			} else {
				pstmt.setNull(8, Types.VARCHAR);
			}

			// 할인율 추가
			// 할인율을 추가 했다면 추가한 할인율로 설정
			if (productSingleVO.getProductSingleDiscount() != 0) {
				pstmt.setInt(9, productSingleVO.getProductSingleDiscount());
				// 아니라면 0으로 설정
			} else {
				pstmt.setInt(9, 0);
			}

			int result = pstmt.executeUpdate();
			System.out.println("[DAO/insert] 상품 등록 완료: " + productSingleVO.getProductSingleName());
			return result > 0;
		} catch (Exception e) {
			System.out.println("[DAO/insert] 상품 등록 중 예외 발생: " + productSingleVO.getProductSingleName());
			e.printStackTrace();
			return false;
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
	}

	// 데이터가 있는지 확인 insert문 때문에
	private boolean existsByName(String productName) {
		String sql = "SELECT COUNT(*) FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NAME = ?";
		// 한번만 사용하기 때문에 내부에 사용
		try (Connection conn = OracleJDBCUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, productName);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0; // 이미 존재함
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; // 예외가 생기거나 없으면 insert 하게끔 처리
	}

	private static final Map<String, Long> storeMap = new HashMap<>();
	static {
		storeMap.put("CU", 10000L);// cu는 10000~
		storeMap.put("GS25", 20000L);// gs25는 20000~
		// 확장성을 위해 다른 store도 추가 쉽게 할 수 있음
	}

	private long getNextProductSingleNumberByBrand(String store) {// 번호생성
		ResultSet rs = null;

		long base = storeMap.getOrDefault(store, 30000L);
		// cu나 gs25 가 아니면 기본값 30000부터 시작
		long maxRange = base + 9999;
		// 해당브랜드가 사용할 최대범위
		long nextNumber = base;
		// 기본으로 시작번호로 초기화
		// 이후 db에 따라 갱신
		try {
			conn = OracleJDBCUtil.connect();
			String sql = "SELECT IFNULL(MAX(PRODUCT_SINGLE_NUMBER), ?) AS MAXNUMBER FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NUMBER BETWEEN ? AND ?";
			// 현재 db에서 해당구간내에서 가장 큰 번호를 조회
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, base - 1);// 데이터가 없을 때는 base-1>>+1하면서 시작
			pstmt.setLong(2, base);// 범위시작
			pstmt.setLong(3, maxRange);// 범위끝

			rs = pstmt.executeQuery();
			if (rs.next()) {
				long max = rs.getLong("MAXNUMBER");
				nextNumber = (max == 0) ? base : max + 1;
				// max가 0이면 base부터, 아니면 +1부터
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
		return nextNumber;
	}

	// U
	public boolean update(ProductSingleVO productSingleVO) {
		return false;
	}

	// D
	public boolean delete(ProductSingleVO productSingleVO) {
		return false;
	}
}