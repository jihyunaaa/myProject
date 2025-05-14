package zic.honeyComboFactory.biz.productSingle.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;

public class OracleProductSingleDAOJdbcTemplate { // 개별 상품 기능 - Oracle DB

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 1. 상품 카테고리(핫이슈, +1 증정상품)
	final String SELECTALLCATEGORY = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE, PRODUCT_SINGLE_CATEGORY, TOTAL_COUNT_NUMBER, PRODUCT_SINGLE_STOCK"
			+ " FROM (SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE,"
			+ " PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE, PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_STOCK, ROW_NUMBER() OVER (ORDER BY PRODUCT_SINGLE_NUMBER DESC) RN,"
			+ " COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ?"
			+ " AND PRODUCT_SINGLE_CATEGORY = ?) WHERE RN BETWEEN ? AND ?";

	// 2. 상품이름 검색(인기순)
	final String SELECTALLSEARCHPOPULAR = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, TOTAL_COUNT_NUMBER, PRODUCT_SINGLE_STOCK FROM (SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME,"
			+ " PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, ROW_NUMBER()"
			+ " OVER (ORDER BY TOTAL_SALES DESC) RN, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
			+ " WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_NAME LIKE '%' || ? || '%') WHERE RN BETWEEN ? AND ?";

	// 3. 상품 전체+인기순
	final String SELECTALLPOPULAR = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, TOTAL_COUNT_NUMBER, TOTAL_SALES, PRODUCT_SINGLE_STOCK FROM (SELECT PRODUCT_SINGLE_NUMBER,"
			+ " PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE,"
			+ " NVL(TOTAL_SALES, 0) AS TOTAL_SALES, PRODUCT_SINGLE_STOCK, ROW_NUMBER() OVER (ORDER BY TOTAL_SALES DESC) RN, COUNT(PRODUCT_SINGLE_NUMBER) OVER()"
			+ " AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ?) WHERE RN BETWEEN ? AND ?";

	// 4. 상품 전체+가격 높은순
	final String SELECTALLPRICEDESC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, TOTAL_COUNT_NUMBER, PRODUCT_SINGLE_STOCK FROM (SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME,"
			+ " PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, ROW_NUMBER()"
			+ " OVER (ORDER BY SINGLE_DISCOUNTED_PRICE DESC) RN, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ?) WHERE RN BETWEEN ? AND ?";

	// 5. 상품 전체+가격 낮은순
	final String SELECTALLPRICEASC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, TOTAL_COUNT_NUMBER, PRODUCT_SINGLE_STOCK FROM (SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME,"
			+ " PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, ROW_NUMBER()"
			+ " OVER (ORDER BY SINGLE_DISCOUNTED_PRICE ASC) RN, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ?) WHERE RN BETWEEN ? AND ?";

	// 6. 상품 카테고리+인기순
	final String SELECTALLCATEGORYPOPULAR = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, TOTAL_COUNT_NUMBER, TOTAL_SALES, PRODUCT_SINGLE_STOCK FROM (SELECT PRODUCT_SINGLE_NUMBER,"
			+ " PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE,"
			+ " TOTAL_SALES, PRODUCT_SINGLE_STOCK, ROW_NUMBER() OVER (ORDER BY TOTAL_SALES DESC) RN, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ?) WHERE RN BETWEEN ? AND ?";

	// 7. 상품 카테고리+가격 높은순
	final String SELECTALLCATEGORYPRICEDESC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, TOTAL_COUNT_NUMBER, PRODUCT_SINGLE_STOCK FROM (SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME,"
			+ " PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, ROW_NUMBER()"
			+ " OVER (ORDER BY SINGLE_DISCOUNTED_PRICE DESC) RN, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ?) WHERE RN BETWEEN ? AND ?";

	// 8. 상품 카테고리+가격 낮은순
	final String SELECTALLCATEGORYPRICEASC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, TOTAL_COUNT_NUMBER, PRODUCT_SINGLE_STOCK FROM (SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME,"
			+ " PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, ROW_NUMBER()"
			+ " OVER (ORDER BY SINGLE_DISCOUNTED_PRICE ASC) RN, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ?) WHERE RN BETWEEN ? AND ?";

	// 9. 상품이름 검색(가격 높은 순)
	final String SELECTALLSEARCHPRICEDESC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, TOTAL_COUNT_NUMBER, PRODUCT_SINGLE_STOCK FROM (SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME,"
			+ " PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, ROW_NUMBER()"
			+ " OVER (ORDER BY SINGLE_DISCOUNTED_PRICE DESC) RN, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_NAME LIKE '%' || ? || '%') WHERE RN BETWEEN ? AND ?";

	// 10. 상품이름 검색(가격 낮은 순)
	final String SELECTALLSEARCHPRICEASC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, TOTAL_COUNT_NUMBER, PRODUCT_SINGLE_STOCK FROM (SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME,"
			+ " PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, ROW_NUMBER()"
			+ " OVER (ORDER BY SINGLE_DISCOUNTED_PRICE ASC) RN, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_NAME LIKE '%' || ? || '%') WHERE RN BETWEEN ? AND ?";

	// 11. 개별상품 재고순 정렬
	final String SELECTALLSTOCKDESC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_STOCK, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE, PRODUCT_SINGLE_CATEGORY,"
			+ " PRODUCT_SINGLE_INFORMATION, TOTAL_COUNT_NUMBER FROM (SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE,"
			+ " PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_STOCK, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE,"
			+ " PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_INFORMATION, ROW_NUMBER() OVER (ORDER BY PRODUCT_SINGLE_STOCK DESC) RN,"
			+ " COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE) WHERE RN BETWEEN ? AND ?";

	// 12. 상품 상세정보
	final String SELECTONE = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
			+ " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, PRODUCT_SINGLE_INFORMATION FROM VIEW_PRODUCT_SINGLE"
			+ " WHERE PRODUCT_SINGLE_NUMBER = ?";

	// 13. 개별상품 추가
	final String INSERT = "INSERT INTO PRODUCT_SINGLE (PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_STOCK,"
			+ " PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE, PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_INFORMATION, PRODUCT_SINGLE_DISCOUNT)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	// 14. 개별 상품 재고 감소
	final String UPDATESTOCKDECREASEONLY = "UPDATE PRODUCT_SINGLE SET PRODUCT_SINGLE_STOCK = PRODUCT_SINGLE_STOCK-? WHERE PRODUCT_SINGLE_NUMBER = ?";

	// 15. 개별 상품 판매가격 및 이름
	final String SELECTONESINGLEDISCOUNTEDPRICEANDSINGLENAME = "SELECT PRODUCT_SINGLE_NAME, SINGLE_DISCOUNTED_PRICE"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NUMBER = ?";

	// 16. 개별상품 재고 및 카테고리 정보
	final String SELECTONESTOCKANDCATEGORY = "SELECT PRODUCT_SINGLE_STOCK, PRODUCT_SINGLE_CATEGORY"
			+ " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NUMBER = ?";
	
	// 17. 개별상품 DB락 + VIEW_PRODUCT_SINGLE에 TOTAL_SALES 쿼리문 있어서 VIEW 테이블 직접 사용 불가능
	final String LOCKPRODUCTSINGLESTOCKFORUPDATE = "SELECT PRODUCT_SINGLE_STOCK FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NUMBER = ? FOR UPDATE";

	// getAll → R
	public List<ProductSingleVO> getAll(ProductSingleVO productSingleVO) {
		// 상품 카테고리(핫이슈, +1 증정상품)
		if (productSingleVO.getCondition().equals("SELECTALLCATEGORY")) {
			System.out.println("상품 카테고리(핫이슈, +1 증정상품)");
			Object[] args = { productSingleVO.getProductSingleStore(), productSingleVO.getProductSingleCategory(),
					productSingleVO.getStartIndex(), productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLCATEGORY, args, new ProductSingleGetAllRowMapper());
		}
		// 상품 카테고리+인기순
		else if (productSingleVO.getCondition().equals("SELECTALLCATEGORYPOPULAR")) {
			System.out.println("상품 카테고리+인기순");
			Object[] args = { productSingleVO.getProductSingleStore(), productSingleVO.getProductSingleCategory(),
					productSingleVO.getStartIndex(), productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLCATEGORYPOPULAR, args, new ProductSingleGetAllRowMapper());
		}
		// 상품 카테고리+가격 높은순
		else if (productSingleVO.getCondition().equals("SELECTALLCATEGORYPRICEDESC")) {
			System.out.println("상품 카테고리+가격 높은순");
			Object[] args = { productSingleVO.getProductSingleStore(), productSingleVO.getProductSingleCategory(),
					productSingleVO.getStartIndex(), productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLCATEGORYPRICEDESC, args, new ProductSingleGetAllRowMapper());
		}
		// 상품 카테고리+가격 낮은순
		else if (productSingleVO.getCondition().equals("SELECTALLCATEGORYPRICEASC")) {
			System.out.println("상품 카테고리+가격 낮은순");
			Object[] args = { productSingleVO.getProductSingleStore(), productSingleVO.getProductSingleCategory(),
					productSingleVO.getStartIndex(), productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLCATEGORYPRICEASC, args, new ProductSingleGetAllRowMapper());
		}
		// 상품이름 검색(인기순)
		else if (productSingleVO.getCondition().equals("SELECTALLSEARCHPOPULAR")) {
			System.out.println("상품이름 검색(인기순)");
			Object[] args = { productSingleVO.getProductSingleStore(), productSingleVO.getSearchKeyword(),
					productSingleVO.getStartIndex(), productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLSEARCHPOPULAR, args, new ProductSingleGetAllRowMapper());
		}
		// 상품이름 검색(가격 높은 순)
		else if (productSingleVO.getCondition().equals("SELECTALLSEARCHPRICEDESC")) {
			System.out.println("상품이름 검색(가격 높은 순)");
			Object[] args = { productSingleVO.getProductSingleStore(), productSingleVO.getSearchKeyword(),
					productSingleVO.getStartIndex(), productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLSEARCHPRICEDESC, args, new ProductSingleGetAllRowMapper());
		}
		// 상품이름 검색(가격 낮은 순)
		else if (productSingleVO.getCondition().equals("SELECTALLSEARCHPRICEASC")) {
			System.out.println("상품이름 검색(가격 낮은 순)");
			Object[] args = { productSingleVO.getProductSingleStore(), productSingleVO.getSearchKeyword(),
					productSingleVO.getStartIndex(), productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLSEARCHPRICEASC, args, new ProductSingleGetAllRowMapper());
		}
		// 상품 전체+인기순
		else if (productSingleVO.getCondition().equals("SELECTALLPOPULAR")) {
			System.out.println("상품 전체+인기순");
			Object[] args = { productSingleVO.getProductSingleStore(), productSingleVO.getStartIndex(),
					productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLPOPULAR, args, new ProductSingleGetAllRowMapper());
		}
		// 상품 전체+가격 높은순
		else if (productSingleVO.getCondition().equals("SELECTALLPRICEDESC")) {
			System.out.println("상품 전체+가격 높은순");
			Object[] args = { productSingleVO.getProductSingleStore(), productSingleVO.getStartIndex(),
					productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLPRICEDESC, args, new ProductSingleGetAllRowMapper());
		}
		// 상품 전체+가격 낮은순
		else if (productSingleVO.getCondition().equals("SELECTALLPRICEASC")) {
			System.out.println("상품 전체+가격 낮은순");
			Object[] args = { productSingleVO.getProductSingleStore(), productSingleVO.getStartIndex(),
					productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLPRICEASC, args, new ProductSingleGetAllRowMapper());
		}
		// 개별상품 재고순 정렬
		else if (productSingleVO.getCondition().equals("SELECTALLSTOCKDESC")) {
			System.out.println("개별상품 재고순 정렬");
			Object[] args = { productSingleVO.getStartIndex(), productSingleVO.getLimitNumber() };
			return jdbcTemplate.query(SELECTALLSTOCKDESC, args, new ProductSingleGetAllRowMapper());
		}
		// 조건에 안맞다면 빈 배열 반환
		else {
			return new ArrayList<>();
		}
	}

	// getOne → R
	public ProductSingleVO getOne(ProductSingleVO productSingleVO) {

		// 상품 상세정보
		if (productSingleVO.getCondition().equals("SELECTONE")) {
			System.out.println("상품 상세정보");
			Object[] args = { productSingleVO.getProductSingleNumber() };
			List<ProductSingleVO> list = jdbcTemplate.query(SELECTONE, args, new ProductSingleGetOneRowMapper());

			return getSingleResult(list);
		}
		// 개별 상품 판매 가격 및 이름
		if (productSingleVO.getCondition().equals("SELECTONESINGLEDISCOUNTEDPRICEANDSINGLENAME")) {
			System.out.println("개별 상품 가격 및 이름");
			Object[] args = { productSingleVO.getProductSingleNumber() };
			List<ProductSingleVO> list = jdbcTemplate.query(SELECTONESINGLEDISCOUNTEDPRICEANDSINGLENAME, args,
					new ProductSingleGetOneDiscountedPriceAndNameRowMapper());

			return getSingleResult(list);
		}
		// 개별상품 재고 및 카테고리 정보
		if (productSingleVO.getCondition().equals("SELECTONESTOCKANDCATEGORY")) {
			System.out.println("개별상품 재고 및 카테고리 정보");
			Object[] args = { productSingleVO.getProductSingleNumber() };
			List<ProductSingleVO> list = jdbcTemplate.query(SELECTONESTOCKANDCATEGORY, args,
					new ProductSingleGetOneStockAndCategoryRowMapper());

			return getSingleResult(list);
		}
		// 개별상품 DB락
		if(productSingleVO.getCondition().equals("LOCKPRODUCTSINGLESTOCKFORUPDATE")) {
			System.out.println("결제 전 DB락");
			System.out.println("개별상품 번호 : ["+productSingleVO.getProductSingleNumber()+"]");
			Object[] args = {productSingleVO.getProductSingleNumber()};
			List<ProductSingleVO> list = jdbcTemplate.query(LOCKPRODUCTSINGLESTOCKFORUPDATE, args, new ProductSingleGetOneLockStockRowMapper());
			
			return getSingleResult(list);
		}
		
		// 조건에 만족하지 못하면 null 반환
		else {
			return null;
		}
	}

	// insert → C
	public boolean insert(ProductSingleVO productSingleVO) {
		System.out.println("상품 등록");
		if (existsByName(productSingleVO.getProductSingleName())) {// 중복 상품 확인 메서드
			System.out.println("[DAO/insert] 중복 상품 등록 생략: " + productSingleVO.getProductSingleName());
			return false;
		}
		if (productSingleVO.getProductSingleNumber() == 0) {// 상품번호 자동 생성
			productSingleVO
					.setProductSingleNumber(getNextProductSingleNumberByBrand(productSingleVO.getProductSingleStore()));
		}

		Object[] args = { productSingleVO.getProductSingleNumber(), productSingleVO.getProductSingleName(),
				productSingleVO.getProductSinglePrice(), productSingleVO.getProductSingleStock(),
				productSingleVO.getProductSingleImage(), productSingleVO.getProductSingleStore(),
				productSingleVO.getProductSingleCategory(),
				(productSingleVO.getProductSingleInformation() != null
						&& !productSingleVO.getProductSingleInformation().isEmpty()
								? productSingleVO.getProductSingleInformation()
								: null),
				(productSingleVO.getProductSingleDiscount() != 0 ? productSingleVO.getProductSingleDiscount() : 0) };

		// 상품 등록
		int result = jdbcTemplate.update(INSERT, args);
		if (result <= 0) {
			System.out.println("상품 등록 실패");
			return false;
		}
		System.out.println("상품 등록 성공");
		return true;
	}
	/*
	 * 상품명이 DB에 이미 존재하는지 확인하는 메서드
	 * - 중복된 상품명이 있으면 true 반환 (insert 차단)
	 * - 예외 발생 시에도 true 반환 (안전하게 insert 차단)
	 *
	 * @param productName 확인할 상품명
	 * @return true: 중복 또는 예외 발생 → insert 차단 / false: 중복 아님 → insert 가능
	 */
	private boolean existsByName(String productName) {
	    String sql = "SELECT COUNT(*) FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NAME = ?";
	    try {
	        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productName);

	        if (count != null) {
	            System.out.println("[DAO/existsByName] 상품명 '" + productName + "' 중복 개수: " + count);
	            return count > 0; // 1개 이상 존재 → 중복
	        } else {
	            System.out.println("[DAO/existsByName] 상품명 '" + productName + "' 확인 결과: null (insert 허용)");
	            return false;
	        }

	    } catch (Exception e) {
	        System.err.println("[DAO/existsByName] 상품명 중복 확인 중 예외 발생: " + e.getMessage());
	        System.err.println("[DAO/existsByName] 예외 상황으로 인해 insert 차단 (상품명: " + productName + ")");
	        return true; // 예외 발생 시 insert를 차단
	    }
	}

	private static final Map<String, Long> storeMap = new HashMap<>();
	static {
		storeMap.put("CU", 10000L);// cu는 10000~
		storeMap.put("GS25", 20000L);// gs25는 20000~
		// 확장성을 위해 다른 store도 추가 쉽게 할 수 있음
	}
	private long getNextProductSingleNumberByBrand(String store) {
	    long base = storeMap.getOrDefault(store, 30000L); // 기본 시작 번호
	    long maxRange = base + 9999;                      // 해당 브랜드 최대 번호
	    long nextNumber = base;                           // 기본값 초기화

	    String sql = "SELECT NVL(MAX(PRODUCT_SINGLE_NUMBER), ?) AS MAXNUMBER "
	               + "FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NUMBER BETWEEN ? AND ?";

	    try {
	        Long max = jdbcTemplate.queryForObject(sql, Long.class, base - 1, base, maxRange);

	        if (max != null) {
	            nextNumber = (max == 0) ? base : max + 1;
	            System.out.println("[DAO/getNextProductSingleNumberByBrand] 브랜드: " + store + ", 현재 최대 번호: " + max + ", 다음 번호: " + nextNumber);
	        } else {
	            System.out.println("[DAO/getNextProductSingleNumberByBrand] 최대 번호 조회 결과 null, 기본값 사용");
	        }

	    } catch (Exception e) {
	        System.err.println("[DAO/getNextProductSingleNumberByBrand] 상품번호 생성 중 예외 발생 - 브랜드: " + store);
	        e.printStackTrace();
	    }

	    return nextNumber;
	}

	// update → U
	public boolean update(ProductSingleVO productSingleVO) {
		int result = 0;
		// 개별 상품 재고 변경
		if (productSingleVO.getCondition().equals("UPDATESTOCKDECREASEONLY")) {
			System.out.println("개별 상품 재고 변경");
			Object[] args = { productSingleVO.getProductSingleCount(), productSingleVO.getProductSingleNumber() };
			result = jdbcTemplate.update(UPDATESTOCKDECREASEONLY, args);
		}

		// 실행된 쿼리가 0 이하라면
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// delete → D
	public boolean delete(ProductSingleVO productSingleVO) {
		return false;
	}

	// 반환 메서드
	private ProductSingleVO getSingleResult(List<ProductSingleVO> list) {
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
}

// 개별 상품 정렬
class ProductSingleGetAllRowMapper implements RowMapper<ProductSingleVO> {

	@Override
	public ProductSingleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductSingleVO data = new ProductSingleVO();

		System.out.println("개별 상품 GetAll Mapper 시작");

		data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
		data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
		data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
		data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
		data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
		data.setProductSingleStock(rs.getInt("PRODUCT_SINGLE_STOCK"));
		// 할인율
		data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
		// 할인된 가격
		data.setProductSingleDiscountedPrice(rs.getInt("SINGLE_DISCOUNTED_PRICE"));

		System.out.println("getAll 개별 상품 데이터 [" + data + "]");
		return data;
	}
}

// 개별 상품 상세정보
class ProductSingleGetOneRowMapper implements RowMapper<ProductSingleVO> {

	@Override
	public ProductSingleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductSingleVO data = new ProductSingleVO();

		data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
		data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
		data.setProductSingleCategory(rs.getString("PRODUCT_SINGLE_CATEGORY"));
		data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
		data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
		data.setProductSingleStock(rs.getInt("PRODUCT_SINGLE_STOCK"));
		data.setProductSingleInformation(rs.getString("PRODUCT_SINGLE_INFORMATION"));
		// 할인율
		data.setProductSingleDiscount(rs.getInt("PRODUCT_SINGLE_DISCOUNT"));
		// 할인된 가격
		data.setProductSingleDiscountedPrice(rs.getInt("SINGLE_DISCOUNTED_PRICE"));

		System.out.println("getOne 개별 상품 상세정보 데이터 [" + data + "]");
		return data;
	}

}

//개별 상품 판매가격 및 이름
class ProductSingleGetOneDiscountedPriceAndNameRowMapper implements RowMapper<ProductSingleVO> {
	@Override
	public ProductSingleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductSingleVO data = new ProductSingleVO();

		data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
		// 할인된 가격
		data.setProductSingleDiscountedPrice(rs.getInt("SINGLE_DISCOUNTED_PRICE"));

		System.out.println("DAO getOne-개별 상품 판매가격 및 이름 : [");
		System.out.println("이름 : " + data.getProductSingleName());
		System.out.println("판매가격 : " + data.getProductSingleDiscountedPrice());
		System.out.println("]");
		return data;
	}
}

//개별상품 재고 및 카테고리 정보
class ProductSingleGetOneStockAndCategoryRowMapper implements RowMapper<ProductSingleVO> {
	@Override
	public ProductSingleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductSingleVO data = new ProductSingleVO();

		data.setProductSingleStock(rs.getInt("PRODUCT_SINGLE_STOCK"));
		data.setProductSingleCategory(rs.getString("PRODUCT_SINGLE_CATEGORY"));

		System.out.println("DAO getOne-개별상품 재고 및 카테고리 정보 : [");
		System.out.println("재고 : " + data.getProductSingleStock());
		System.out.println("카테고리 : " + data.getProductSingleCategory());
		System.out.println("]");
		return data;
	}
}
// 개별상품 DB락
class ProductSingleGetOneLockStockRowMapper implements RowMapper<ProductSingleVO>{
	@Override
	public ProductSingleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductSingleVO data = new ProductSingleVO();
		
		data.setProductSingleStock(rs.getInt("PRODUCT_SINGLE_STOCK"));
		
		System.out.println("재고 수 파악 ["+data.getProductSingleStock()+"] 개");
		
		return data;
	}
}

//23ai 버전 OFFSET ? ROWS FETCH NEXT ? ROWS ONLY을 사용하였으나 11g에서는 사용 불가

/*
 * // 1. 상품 카테고리(핫이슈, +1 증정상품) final String SELECTALLCATEGORY =
 * "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
 * +
 * " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
 * +
 * " WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // 2. 상품이름 검색(인기순) final String SELECTALLSEARCHPOPULAR =
 * "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
 * +
 * " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
 * +
 * " WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_NAME LIKE '%' || ? || '%' OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // 3. 상품 전체+인기순 final String SELECTALLPOPULAR =
 * "SELECT VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NAME,"
 * +
 * " VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE, VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_DISCOUNT, VIEW_PRODUCT_SINGLE.SINGLE_DISCOUNTED_PRICE,"
 * +
 * " VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE, COUNT(VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, COUNT(PURCHASE_DETAIL.PURCHASE_DETAIL_NUMBER)"
 * +
 * " AS PURCHASE_COUNT FROM VIEW_PRODUCT_SINGLE LEFT JOIN PURCHASE_DETAIL ON VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PURCHASE_DETAIL.PRODUCT_SINGLE_NUMBER"
 * +
 * " WHERE VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_STORE = ? GROUP BY VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NAME, VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE,"
 * +
 * " VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_DISCOUNT, VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE, VIEW_PRODUCT_SINGLE.SINGLE_DISCOUNTED_PRICE ORDER BY PURCHASE_COUNT DESC"
 * + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
 * 
 * 
 * // 4. 상품 전체+가격 높은순 final String SELECTALLPRICEDESC =
 * "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
 * +
 * " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
 * +
 * " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? ORDER BY SINGLE_DISCOUNTED_PRICE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // 5. 상품 전체+가격 낮은순 final String SELECTALLPRICEASC =
 * "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
 * +
 * " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
 * +
 * " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? ORDER BY SINGLE_DISCOUNTED_PRICE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // 6. 상품 카테고리+인기순 final String SELECTALLCATEGORYPOPULAR =
 * "SELECT VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NAME,"
 * +
 * " VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE, VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_DISCOUNT, VIEW_PRODUCT_SINGLE.SINGLE_DISCOUNTED_PRICE,"
 * +
 * " VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE, COUNT(VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, COUNT(PURCHASE_DETAIL.PURCHASE_DETAIL_NUMBER)"
 * +
 * " AS PURCHASE_COUNT FROM VIEW_PRODUCT_SINGLE LEFT JOIN PURCHASE_DETAIL ON VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PURCHASE_DETAIL.PRODUCT_SINGLE_NUMBER"
 * +
 * " WHERE VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_STORE = ? AND VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_CATEGORY = ? GROUP BY VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER,"
 * +
 * " VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_NAME, VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE, VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_DISCOUNT, VIEW_PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE,"
 * +
 * " VIEW_PRODUCT_SINGLE.SINGLE_DISCOUNTED_PRICE ORDER BY PURCHASE_COUNT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * 
 * // 7. 상품 카테고리+가격 높은순 final String SELECTALLCATEGORYPRICEDESC =
 * "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE," +
 * " PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER()"
 * +
 * " AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ?"
 * +
 * " ORDER BY SINGLE_DISCOUNTED_PRICE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // 8. 상품 카테고리+가격 낮은순 final String SELECTALLCATEGORYPRICEASC =
 * "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
 * +
 * " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
 * +
 * " WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ? ORDER BY SINGLE_DISCOUNTED_PRICE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // 9. 상품이름 검색(가격 높은 순) final String SELECTALLSEARCHPRICEDESC =
 * "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
 * +
 * " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
 * +
 * " WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_NAME LIKE '%' || ? || '%' ORDER BY SINGLE_DISCOUNTED_PRICE DESC"
 * + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
 * 
 * // 10. 상품이름 검색(가격 낮은 순) final String SELECTALLSEARCHPRICEASC =
 * "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
 * +
 * " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
 * +
 * " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_NAME LIKE '%' || ? || '%' ORDER BY"
 * + " SINGLE_DISCOUNTED_PRICE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
 * 
 * // 11. 개별상품 재고순 정렬 final String SELECTALLSTOCKDESC =
 * "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_DISCOUNT,"
 * +
 * " SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_STOCK, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE, PRODUCT_SINGLE_CATEGORY,"
 * +
 * " PRODUCT_SINGLE_INFORMATION, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM VIEW_PRODUCT_SINGLE"
 * + " ORDER BY PRODUCT_SINGLE_STOCK DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
 * 
 * // 12. 상품 상세정보 final String SELECTONE =
 * "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_PRICE,"
 * +
 * " PRODUCT_SINGLE_DISCOUNT, SINGLE_DISCOUNTED_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, PRODUCT_SINGLE_INFORMATION"
 * + " FROM VIEW_PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NUMBER = ?";
 * 
 * // 13. 개별상품 추가 final String INSERT =
 * "INSERT INTO PRODUCT_SINGLE (PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_STOCK, "
 * +
 * "PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE, PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_INFORMATION, PRODUCT_SINGLE_DISCOUNT) "
 * + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
 * 
 */
