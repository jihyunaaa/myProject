package zic.honeyComboFactory.view.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import zic.honeyComboFactory.biz.productComboVO.ProductComboVO;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleService;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;

@Controller
public class ProductSingleController {

	@Autowired
	private ProductSingleService productSingleService;

	// 핫이슈 상품 불러오기 기능
	@GetMapping("/product/hotIssue")
	@ResponseBody
	public List<Map<String, Object>> getHotIssueProduct(ProductSingleVO productSingleVO, ProductComboVO productComboVO,
			@RequestParam(value = "hotCUProductPageNumber", required = false) Integer cuPage,
			@RequestParam(value = "hotCUProductContentCount", required = false) Integer cuCount,
			@RequestParam(value = "hotGSProductPageNumber", required = false) Integer gsPage,
			@RequestParam(value = "hotGSProductContentCount", required = false) Integer gsCount) {
		String store = null;
		int pageNumber = 0;
		int contentCount = 0;

		// CU, GS 브랜드 및 페이징 처리
		if (cuPage != null) {
			store = "CU";
			pageNumber = cuPage;
			contentCount = cuCount;
		} else if (gsPage != null) {
			store = "GS25";
			pageNumber = gsPage;
			contentCount = gsCount;
		}

		System.out.println("브랜드 [" + store + "]");
		System.out.println("더보기 횟수 [" + pageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + contentCount + "]");

		int startIndex = ((pageNumber - 1) * contentCount)+1;
		int endRow = contentCount * pageNumber;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");
		System.out.println("마지막 인덱스 번호 [" + endRow + "]");

		// VO 객체 설정
		productSingleVO.setCondition("SELECTALLCATEGORY");
		productSingleVO.setProductSingleStore(store);
		productSingleVO.setProductSingleCategory("HOTISSUE");
		productSingleVO.setStartIndex(startIndex);
		productSingleVO.setLimitNumber(endRow);

		// 서비스 계층에서 핫이슈 상품 목록 조회
		List<ProductSingleVO> productList = productSingleService.getAll(productSingleVO);
		// JSON 응답용 List 생성 (Map 하나 = 상품 하나)
		List<Map<String, Object>> jsonResponse = new ArrayList<>();
		// 품절 시 이미지 경로 변경하여 응답 세팅
		returnApplyProductSingleSoldoutImgPathMapList(jsonResponse, productList);

		return jsonResponse; // @ResponseBody : 자동으로 JSON 반환
	}

	// +1 증정상품 불러오기 기능
	@GetMapping("/product/plusOne")
	@ResponseBody
	public List<Map<String, Object>> getPlusOneProduct(ProductSingleVO productSingleVO,
			@RequestParam(value = "plusCUProductPageNumber", required = false) Integer cuPage,
			@RequestParam(value = "plusCUProductContentCount", required = false) Integer cuCount,
			@RequestParam(value = "plusGSProductPageNumber", required = false) Integer gsPage,
			@RequestParam(value = "plusGSProductContentCount", required = false) Integer gsCount) {
		String store = null;
		int pageNumber = 0;
		int contentCount = 0;

		if (cuPage != null) {
			store = "CU";
			pageNumber = cuPage;
			contentCount = cuCount;
		} else if (gsPage != null) {
			store = "GS25";
			pageNumber = gsPage;
			contentCount = gsCount;
		}

		int startIndex = ((pageNumber - 1) * contentCount)+1;
		int endRow = contentCount * pageNumber;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");
		System.out.println("마지막 인덱스 번호 [" + endRow + "]");

		productSingleVO.setCondition("SELECTALLCATEGORY");
		productSingleVO.setProductSingleStore(store);
		productSingleVO.setProductSingleCategory("PLUSPRODUCT");
		productSingleVO.setStartIndex(startIndex);
		productSingleVO.setLimitNumber(endRow);

		List<ProductSingleVO> productList = productSingleService.getAll(productSingleVO);
		List<Map<String, Object>> jsonResponse = new ArrayList<>();
		// 품절 시 이미지 경로 변경하여 응답 세팅
		returnApplyProductSingleSoldoutImgPathMapList(jsonResponse, productList);

		return jsonResponse;
	}

	// 모듈화 메서드
	// call by reference로 리스트 반환 안해도 됨
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
			productMap.put("productSingleStock", product.getProductSingleStock()); // 재고
			productMap.put("productSingleDiscount", product.getProductSingleDiscount()); // 할인율
			productMap.put("productSingleDiscountedPrice", product.getProductSingleDiscountedPrice()); // 할인율 적용 가격
			productMap.put("totalCountNumber", product.getTotalCountNumber()); // 총 개수
			response.add(productMap); // 결과 리스트에 추가
		}
	}
}
