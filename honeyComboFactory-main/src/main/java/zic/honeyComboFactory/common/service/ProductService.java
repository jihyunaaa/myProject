package zic.honeyComboFactory.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.productComboComponentVO.ProductComboComponentService;
import zic.honeyComboFactory.biz.productComboComponentVO.ProductComboComponentVO;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleService;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;

@Service
public class ProductService { // 상품 서비스

	@Autowired
	private ProductSingleService productSingleService;
	@Autowired
	private ProductComboComponentService productComboComponentService;

	// 상품 타입(개별, 꿀조합) 여부 구분 후 재고 차감 기능
	public boolean updateProductStock(List<Map<String, Object>> orderList) {
		boolean flag = true;

		// 상품 DB 재고 변경
		for (Map<String, Object> item : orderList) {
			long productNumber = Long.parseLong(item.get("productNumber").toString());
			int count = Integer.parseInt(item.get("count").toString());
			boolean isComboProduct = Boolean.parseBoolean(item.get("isComboProduct").toString());

			// 꿀조합 상품이라면
			if (isComboProduct) {
				// 개별상품 재고변경 로직 수행
				flag = updateComboProductStock(productNumber, count);
			}
			// 개별 상품이라면
			else {
				// 개별상품 재고변경 로직 수행
				flag = updateSingleProductStock(productNumber, count);
			}

			// 재고 변경에 실패했다면
			if (!flag) {
				return false;
			}
		}
		return true;
	}

	// 꿀조합 상품일 때 재고 차감을 위한 밑준비 로직
	private boolean updateComboProductStock(long productNumber, int count) {
		ProductComboComponentVO productComboComponentVO = new ProductComboComponentVO();
		productComboComponentVO.setCondition("SELECTONECOMPONENTNUMBER");
		productComboComponentVO.setProductComboNumber(productNumber);
		productComboComponentVO = this.productComboComponentService.getOne(productComboComponentVO);

		// 조합 상품에 포함된 개별 상품 번호 추출
		List<Long> componentList = Arrays
				.asList(productComboComponentVO.getProductComboComponentOne(),
						productComboComponentVO.getProductComboComponentTwo(),
						productComboComponentVO.getProductComboComponentThree())
				.stream().filter(num -> num != null && num != 0).collect(Collectors.toList());

		// 구성품 정보 리스트에 담기
		List<ProductSingleVO> productList = new ArrayList<>();
		for (Long componentNumber : componentList) {
			ProductSingleVO productSingleVO = new ProductSingleVO();
			productSingleVO.setCondition("SELECTONESTOCKANDCATEGORY");
			productSingleVO.setProductSingleNumber(componentNumber);
			productSingleVO = this.productSingleService.getOne(productSingleVO);
			productSingleVO.setProductSingleNumber(componentNumber);
			productList.add(productSingleVO);
		}

		// 개별 상품마다 재고 차감
		for (ProductSingleVO product : productList) {
			// 꿀조합 상품 구성품 번호
			Long productComponentNumber = product.getProductSingleNumber();
			// 상품 카테고리
			String category = product.getProductSingleCategory();
			// 구매 수량
			int decreaseStock = category.equals("PLUSPRODUCT") ? count * 2 : count;

			// 개별 상품 재고 차감
			boolean flag = updateStock(productComponentNumber, decreaseStock);
			System.out.println("꿀조합 구성품 DB 재고 차감 성공여부 : [" + flag + "]");
			// 재고 차감 실패 시
			if (!flag) {
				return false;
			}
		}
		return true;
	}

	// 개별 상품일 때 재고 차감을 위한 밑준비 로직
	private boolean updateSingleProductStock(long productNumber, int count) {
		// 개별 상품 정보 조회 (단 한 번)
		ProductSingleVO productVO = new ProductSingleVO();
		productVO.setCondition("SELECTONESTOCKANDCATEGORY");
		productVO.setProductSingleNumber(productNumber);
		productVO = this.productSingleService.getOne(productVO);

		// 상품 카테고리
		String category = productVO.getProductSingleCategory();
		// 구매 수량
		int decreaseStock = category.equals("PLUSPRODUCT") ? count * 2 : count;

		// 개별 상품 재고 차감
		boolean flag = updateStock(productNumber, decreaseStock);
		System.out.println("개별상품 DB 재고 차감 성공여부 : [" + flag + "]");

		return flag;
	}

	// DB 건드려서 재고 차감 기능
	private boolean updateStock(long productNumber, int decreaseStock) {
		ProductSingleVO productSingleVO = new ProductSingleVO();
		productSingleVO.setCondition("UPDATESTOCKDECREASEONLY");
		productSingleVO.setProductSingleNumber(productNumber);
		productSingleVO.setProductSingleCount(decreaseStock);
		System.out.println("재고 변경할 상품 정보 : [" + productSingleVO + "]");
		return this.productSingleService.update(productSingleVO);
	}

	// 상품 재고 검사 기능
	public boolean checkStock(String cartProductNumberDatas, HttpSession session) {
		System.out.println("구매가능 확인용 상품 번호 : [" + cartProductNumberDatas + "]");

		// "+"로 연결된 상품 번호 분리
		String[] productNumbers = cartProductNumberDatas.split("\\+");
		// 구매할 상품번호 로그 찍기
		for (String productNumber : productNumbers) {
			System.out.println("+분리한 상품 번호 : [" + productNumber + "]");
		}

		// 1. productNumber로 바로 찾을 수 있는 Map 만들기
		ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session
				.getAttribute("shoppingCart");
		// 장바구니에서 키값이 productNumber인 정보만 빼서 Map 새로 생성
		Map<String, Map<String, Object>> cartMap = new HashMap<String, Map<String, Object>>();
		for (Map<String, Object> item : shoppingCart) {
			cartMap.put(item.get("productNumber").toString(), item);
		}

		// 2. 새로 생성한 Map에 반복해서 바로 찾기
		// 상품 정보 저장할 배열 orderList 선언
		List<Map<String, Object>> orderList = new ArrayList<>();
		// orderList에 정보 담기
		for (String number : productNumbers) {
			Map<String, Object> item = cartMap.get(number);
			if (item == null) { // 혹시라도 장바구니에 없는 상품번호를 대비
				return false;
			}

			// 필요한 정보만 뽑아 새로운 Map에 담기
			Map<String, Object> newItem = new HashMap<>();
			newItem.put("productNumber", number);
			newItem.put("cartProductCount", item.get("cartProductCount"));
			newItem.put("isComboProduct", item.get("isComboProduct"));

			orderList.add(newItem);
		}

		System.out.println("재고 검사할 상품 목록 정보 : [" + orderList + "]");
		// 상품 재고 검사
		for (Map<String, Object> item : orderList) {
			long productNumber = Long.parseLong(item.get("productNumber").toString());
			int count = Integer.parseInt(item.get("cartProductCount").toString());
			boolean isComboProduct = Boolean.parseBoolean(item.get("isComboProduct").toString());
			if (isComboProduct) { // 꿀조합 상품이라면
				ProductComboComponentVO productComboComponentVO = new ProductComboComponentVO();
				productComboComponentVO.setCondition("SELECTONECOMPONENTNUMBER");
				productComboComponentVO.setProductComboNumber(productNumber);
				productComboComponentVO = this.productComboComponentService.getOne(productComboComponentVO);
				// 조합 상품에 포함된 개별 상품 번호 추출
				List<Long> componentList = Arrays
						.asList(productComboComponentVO.getProductComboComponentOne(),
								productComboComponentVO.getProductComboComponentTwo(),
								productComboComponentVO.getProductComboComponentThree())
						.stream().filter(num -> num != null && num != 0).collect(Collectors.toList());

				System.out.println("꿀조합 상품일 경우 재고 검사할 상품번호 목록 : [" + componentList + "]");
				// 개별 상품마다 재고 확인
				for (Long componentNumber : componentList) {
					ProductSingleVO product = new ProductSingleVO();
					product.setCondition("SELECTONESTOCKANDCATEGORY");
					product.setProductSingleNumber(componentNumber);
					product = this.productSingleService.getOne(product);

					// 현재 상품 재고 수
					int stock = product.getProductSingleStock();
					// 상품 카테고리
					String category = product.getProductSingleCategory();
					// 구매 수량
					int requiredStock = category.equals("PLUSPRODUCT") ? count * 2 : count;

					// 재고 부족 시
					if (stock < requiredStock) {
						System.out.println("꿀조합 구성품 재고 부족");
						return false;
					}
				}
			} else { // 개별 상품이라면
				// 개별 상품 정보 조회 (단 한 번)
				ProductSingleVO product = new ProductSingleVO();
				product.setCondition("SELECTONESTOCKANDCATEGORY");
				product.setProductSingleNumber(productNumber);
				product = this.productSingleService.getOne(product);

				// 현재 상품 재고 수
				int stock = product.getProductSingleStock();
				// 상품 카테고리
				String category = product.getProductSingleCategory();
				// 구매 수량
				int requiredStock = category.equals("PLUSPRODUCT") ? count * 2 : count;

				// 개별 상품 재고 확인
				if (stock < requiredStock) {
					System.out.println("개별 상품 재고 부족");
					return false;
				}
			}
		}
		return true;
	}

	// 상품 재고 DB락 기능
	public boolean lockProductSingleStockForUpdate(List<Long> sortedCartProductNumberDatas) {
		System.out.println("상품재고 DB락 걸기용 상품 번호 : [" + sortedCartProductNumberDatas + "]");

		// 꿀조합 상품 시작번호
		final long productComboStartNumber = 30000;
		// 개별상품 번호만 담을 배열 선언
		List<Long> cartProductSingleNumberDatas = new ArrayList<>();
		// 꿀조합 상품 번호만 담을 배열 선언
		List<Long> cartProductComboNumberDatas = new ArrayList<>();

		// 상품 종류(개별,꿀조합) 번호 분리
		for (long cartProductNumber : sortedCartProductNumberDatas) {
			// 꿀조합 상품번호라면
			if (productComboStartNumber <= cartProductNumber) {
				cartProductComboNumberDatas.add(cartProductNumber);
			} else { // 개별상품 번호라면
				cartProductSingleNumberDatas.add(cartProductNumber);
			}
		}
		
		System.out.println("1차 분리된 꿀조합 상품 번호 목록 : ["+cartProductComboNumberDatas+"]");
		System.out.println("1차 분리된 개별 상품 번호 목록 : ["+cartProductSingleNumberDatas+"]");

		// 꿀조합 상품을 구성하는 개별상품 번호 뽑아오기
		for (long cartProductComboNumber : cartProductComboNumberDatas) {
			ProductComboComponentVO productComboComponentVO = new ProductComboComponentVO();
			productComboComponentVO.setCondition("SELECTONECOMPONENTNUMBER");
			productComboComponentVO.setProductComboNumber(cartProductComboNumber);
			productComboComponentVO = this.productComboComponentService.getOne(productComboComponentVO);

			// 꿀조합 상품에 포함된 개별 상품 번호 추출
			List<Long> componentList = Arrays
					.asList(productComboComponentVO.getProductComboComponentOne(),
							productComboComponentVO.getProductComboComponentTwo(),
							productComboComponentVO.getProductComboComponentThree())
					.stream().filter(num -> num != null && num != 0).collect(Collectors.toList());
			System.out.println("꿀조합 상품일 경우 DB락 걸을 상품번호 목록 : [" + componentList + "]");

			// 위에서 선언한 개별상품 번호만 담을 배열에 추가
			cartProductSingleNumberDatas.addAll(componentList);
		}

		System.out.println("최종 DB락 걸을 개별 상품 번호 목록 : ["+cartProductSingleNumberDatas+"]");
		// 개별상품 재고 DB락 걸기
		for (long cartProductSingleNumber : cartProductSingleNumberDatas) {
			ProductSingleVO productSingleVO = new ProductSingleVO();
			productSingleVO.setCondition("LOCKPRODUCTSINGLESTOCKFORUPDATE");
			productSingleVO.setProductSingleNumber(cartProductSingleNumber);
			productSingleVO = this.productSingleService.getOne(productSingleVO);

			// DB락 성공 여부 반환
			return productSingleVO != null;
		}

		return true;
	}
}
