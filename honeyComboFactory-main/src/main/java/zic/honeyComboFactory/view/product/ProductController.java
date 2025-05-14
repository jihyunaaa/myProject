package zic.honeyComboFactory.view.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import zic.honeyComboFactory.biz.productComboVO.ProductComboService;
import zic.honeyComboFactory.biz.productComboVO.ProductComboVO;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleService;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;

@RestController
public class ProductController {

	@Autowired
	private ProductSingleService productSingleService;
	@Autowired
	private ProductComboService productComboService;

	// 상품 정렬 및 카테고리 기능
	@GetMapping("/product/order")
	public List<Map<String, Object>> OrderProduct(ProductSingleVO productSingleVO, ProductComboVO productComboVO,
			@RequestParam(value = "CUProductPageNumber", required = false) Integer cuProductPage,
			@RequestParam(value = "CUProductContentCount", required = false) Integer cuProductCount,
			@RequestParam(value = "CUProductOrderCondition", required = false) String cuCondition,
			@RequestParam(value = "CUProductCategory", required = false) String cuCategory,
			@RequestParam(value = "GSProductPageNumber", required = false) Integer gsProductPage,
			@RequestParam(value = "GSProductContentCount", required = false) Integer gsProductCount,
			@RequestParam(value = "GSProductOrderCondition", required = false) String gsCondition,
			@RequestParam(value = "GSProductCategory", required = false) String gsCategory,
			@RequestParam(value = "comboProductPageNumber", required = false) Integer comboPage,
			@RequestParam(value = "comboProductContentCount", required = false) Integer comboCount,
			@RequestParam(value = "comboProductOrderCondition", required = false) String comboCondition,
			@RequestParam(value = "comboProductCategory", required = false) String comboCategory) {
		String store = null;
		String orderCondition = null;
		String category = null;
		int pageNumber = 0;
		int contentCount = 0;

		System.out.println("CUProductPageNumber: " + cuProductPage);
		System.out.println("GSProductPageNumber: " + gsProductPage);
		System.out.println("comboProductPageNumber: " + comboPage);
		System.out.println("store: " + store);

		// JS에서 보내는 값에 따라 조건 확인

		// CU상품을 요청한다면
		if (cuProductPage != null) {
			store = "CU";
			pageNumber = cuProductPage;
			contentCount = cuProductCount;
			orderCondition = cuCondition;
			category = cuCategory;
		} else if (gsProductPage != null) {
			store = "GS25";
			pageNumber = gsProductPage;
			contentCount = gsProductCount;
			orderCondition = gsCondition;
			category = gsCategory;
		} else if (comboPage != null) {
			pageNumber = comboPage;
			contentCount = comboCount;
			orderCondition = comboCondition;
			category = comboCategory;
		}

		System.out.println("페이지 번호 [" + pageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + contentCount + "]");
		System.out.println("정렬 조건 [" + orderCondition + "]");
		System.out.println("카테고리 [" + category + "]");

		if (store != null) {
			System.out.println("스토어 [" + store + "]");
		}

		int startIndex = ((pageNumber - 1) * contentCount)+1;
		int endRow = contentCount * pageNumber;
		System.out.println("시작 인덱스 번호 : [" + startIndex + "]");
		System.out.println("마지막 인덱스 번호 : [" + endRow + "]");

		List<Map<String, Object>> jsonResponse = new ArrayList<>();
		// CU / GS 단일 상품
		if (store != null) {
			productSingleVO.setProductSingleStore(store);
			productSingleVO.setStartIndex(startIndex);
			productSingleVO.setLimitNumber(endRow);

			if ("ORDERPOPULAR".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productSingleVO.setCondition("SELECTALLPOPULAR");
			} else if ("ORDERHIGHPRICES".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productSingleVO.setCondition("SELECTALLPRICEDESC");
			} else if ("ORDERLOWPRICES".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productSingleVO.setCondition("SELECTALLPRICEASC");
			} else {
				productSingleVO.setProductSingleCategory(category);
				if ("ORDERPOPULAR".equals(orderCondition)) {
					productSingleVO.setCondition("SELECTALLCATEGORYPOPULAR");
				} else if ("ORDERHIGHPRICES".equals(orderCondition)) {
					productSingleVO.setCondition("SELECTALLCATEGORYPRICEDESC");
				} else if ("ORDERLOWPRICES".equals(orderCondition)) {
					productSingleVO.setCondition("SELECTALLCATEGORYPRICEASC");
				}
			}

			List<ProductSingleVO> productList = productSingleService.getAll(productSingleVO);
			// 품절 시 이미지 경로 변경하여 응답 세팅
			returnApplyProductSingleSoldoutImgPathMapList(jsonResponse, productList);
		}
		// 콤보 상품
		else {
			productComboVO.setProductComboIndex(startIndex);
			productComboVO.setProductComboContentCount(endRow);

			if ("ORDERPOPULAR".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productComboVO.setCondition("SELECTALLPOPULAR");
			} else if ("ORDERHIGHPRICES".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productComboVO.setCondition("SELECTALLPRICEDESC");
			} else if ("ORDERLOWPRICES".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productComboVO.setCondition("SELECTALLPRICEASC");
			} else {
				if ("MDPRODUCT".equals(category)) {
					productComboVO.setProductComboCategory("MD");
				} else if ("CELEBRITYPRODUCT".equals(category)) {
					productComboVO.setProductComboCategory("INFLUENCER");
				} else if ("COSTEFFECTIVENESSPRODUCT".equals(category)) {
					productComboVO.setProductComboCategory("CHEAP");
				}

				if ("ORDERPOPULAR".equals(orderCondition)) {
					productComboVO.setCondition("SELECTALLCATEGORYPOPULAR");
				} else if ("ORDERHIGHPRICES".equals(orderCondition)) {
					productComboVO.setCondition("SELECTALLCATEGORYDESC");
				} else if ("ORDERLOWPRICES".equals(orderCondition)) {
					productComboVO.setCondition("SELECTALLCATEGORYASC");
				}
			}

			List<ProductComboVO> productList = productComboService.getAll(productComboVO);
			// 품절 시 이미지 경로 변경하여 응답 세팅
			returnApplyProductComboSoldoutImgPathMapList(jsonResponse, productList);
		}
		return jsonResponse;
	}

	// 상품 이름 검색 기능
	@GetMapping("/product/search")
	@ResponseBody
	public List<Map<String, Object>> searchProducts(ProductSingleVO productSingleVO, ProductComboVO productComboVO,
			@RequestParam(value = "CUProductPageNumber", required = false) Integer cuPage,
			@RequestParam(value = "CUProductContentCount", required = false) Integer cuCount,
			@RequestParam(value = "CUProductOrderCondition", required = false) String cuCondition,
			@RequestParam(value = "GSProductPageNumber", required = false) Integer gsPage,
			@RequestParam(value = "GSProductContentCount", required = false) Integer gsCount,
			@RequestParam(value = "GSProductOrderCondition", required = false) String gsCondition,
			@RequestParam(value = "comboProductPageNumber", required = false) Integer comboPage,
			@RequestParam(value = "comboProductContentCount", required = false) Integer comboCount,
			@RequestParam(value = "comboProductOrderCondition", required = false) String comboCondition) {
		String store = null;
		String orderCondition = null;
		int pageNumber = 0;
		int contentCount = 0;

		if (cuPage != null) {
			store = "CU";
			pageNumber = cuPage;
			contentCount = cuCount;
			orderCondition = cuCondition;
		} else if (gsPage != null) {
			store = "GS25";
			pageNumber = gsPage;
			contentCount = gsCount;
			orderCondition = gsCondition;
		} else if (comboPage != null) {
			pageNumber = comboPage;
			contentCount = comboCount;
			orderCondition = comboCondition;
		}

		int startIndex = ((pageNumber - 1) * contentCount)+1;
		int endRow = contentCount * pageNumber;

		List<Map<String, Object>> jsonResponse = new ArrayList<>();
		if (store != null) {
			productSingleVO.setProductSingleStore(store);
			productSingleVO.setStartIndex(startIndex);
			productSingleVO.setLimitNumber(endRow);

			if ("ORDERPOPULAR".equals(orderCondition)) {
				productSingleVO.setCondition("SELECTALLSEARCHPOPULAR");
			} else if ("ORDERHIGHPRICES".equals(orderCondition)) {
				productSingleVO.setCondition("SELECTALLSEARCHPRICEDESC");
			} else if ("ORDERLOWPRICES".equals(orderCondition)) {
				productSingleVO.setCondition("SELECTALLSEARCHPRICEASC");
			}

			List<ProductSingleVO> productList = productSingleService.getAll(productSingleVO);
			// 품절 시 이미지 경로 변경하여 응답 세팅
			returnApplyProductSingleSoldoutImgPathMapList(jsonResponse, productList);
		} else {
			productComboVO.setProductComboIndex(startIndex);
			productComboVO.setProductComboContentCount(contentCount);

			if ("ORDERPOPULAR".equals(orderCondition)) {
				productComboVO.setCondition("SELECTALLSEARCHPOPULAR");
			} else if ("ORDERHIGHPRICES".equals(orderCondition)) {
				productComboVO.setCondition("SELECTALLSEARCHDESC");
			} else if ("ORDERLOWPRICES".equals(orderCondition)) {
				productComboVO.setCondition("SELECTALLSEARCHASC");
			}

			List<ProductComboVO> productList = productComboService.getAll(productComboVO);
			// 품절 시 이미지 경로 변경하여 응답 세팅
			returnApplyProductComboSoldoutImgPathMapList(jsonResponse, productList);
		}
		return jsonResponse;
	}

	// 컴포넌트화 메서드
	// call by reference로 리스트 반환 안해도 됨
	// 꿀조합 상품
	// 품절 시 이미지 경로 변경하여 Map 세팅 기능
	private void returnApplyProductComboSoldoutImgPathMapList(List<Map<String, Object>> response,
			List<ProductComboVO> list) {
		final String soldoutImgLink = "assets/img/product/soldout.png";

		for (ProductComboVO product : list) {
			Map<String, Object> productComboMap = new HashMap<String, Object>();

			// 품절상품 이미지 경로 변경
			int stock = product.getProductComboStock();
			if (stock <= 0) { // 품절이라면
				product.setProductComboImage(soldoutImgLink);
			}

			productComboMap.put("productComboNumber", product.getProductComboNumber()); // 상품 번호
			productComboMap.put("productComboImage", product.getProductComboImage()); // 이미지
			productComboMap.put("productComboName", product.getProductComboName()); // 상품 이름
			productComboMap.put("productComboPrice", product.getProductComboPrice()); // 가격
			productComboMap.put("productComboDiscount", product.getProductComboDiscount()); // 할인율
			productComboMap.put("productComboDiscountedPrice", product.getProductComboDiscountedPrice()); // 할인율 적용 가격
			productComboMap.put("totalCountNumber", product.getTotalCountNumber()); // 총 개수
			response.add(productComboMap); // 결과 리스트에 추가
		}
	}

	// 개별 상품
	// 품절 시 이미지 경로 변경하여 Map 세팅 기능
	private void returnApplyProductSingleSoldoutImgPathMapList(List<Map<String, Object>> response,
			List<ProductSingleVO> list) {
		final String soldoutImgLink = "assets/img/product/soldout.png";

		for (ProductSingleVO product : list) {
			Map<String, Object> productMap = new HashMap<String, Object>();

			// 품절상품 이미지 경로 변경
			int stock = product.getProductSingleStock();
			if (stock <= 0) { // 품절이라면
				product.setProductSingleImage(soldoutImgLink);
			}

			productMap.put("productSingleNumber", product.getProductSingleNumber()); // 상품 번호
			productMap.put("productSingleImage", product.getProductSingleImage()); // 이미지
			productMap.put("productSingleName", product.getProductSingleName()); // 상품 이름
			productMap.put("productSinglePrice", product.getProductSinglePrice()); // 가격
			productMap.put("productSingleDiscount", product.getProductSingleDiscount()); // 할인율
			productMap.put("productSingleDiscountedPrice", product.getProductSingleDiscountedPrice()); // 할인율 적용 가격
			productMap.put("totalCountNumber", product.getTotalCountNumber()); // 총 개수
			response.add(productMap); // 결과 리스트에 추가
		}
	}
}
