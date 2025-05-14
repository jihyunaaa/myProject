package zic.honeyComboFactory.biz.productComboVO;

public class ProductComboVO { // ProductComboDTO
	// 테이블 컬럼
	private long productComboNumber;
	private String productComboName;
	private String productComboImage;
	private int productComboStore; // 1. 씨유 2. gs
	private String productComboCategory; // 1. md 2. 인플루언서 3. 갓성비
	private String productComboInformation;
	// 추가 멤버변수
	private int productComboPrice;
	private int productComboStock;
	private int productComboCount; // 장바구니 상품 개수
	private int productComboIndex;
	private int productComboContentCount;
	private int productComboADNumber; // 광고 번호
	private long totalCountNumber; // 총 데이터 개수
	private String condition;
	private String searchKeyword;
	
	// 최프때 추가된 VO
	private int productComboDiscount; // 할인율
	private int productComboDiscountedPrice; // 할인율 적용된 가격
	
	// getter, setter
	public long getProductComboNumber() {
		return productComboNumber;
	}

	public void setProductComboNumber(long productComboNumber) {
		this.productComboNumber = productComboNumber;
	}

	public String getProductComboName() {
		return productComboName;
	}

	public void setProductComboName(String productComboName) {
		this.productComboName = productComboName;
	}

	public String getProductComboImage() {
		return productComboImage;
	}

	public void setProductComboImage(String productComboImage) {
		this.productComboImage = productComboImage;
	}

	public int getProductComboStore() {
		return productComboStore;
	}

	public void setProductComboStore(int productComboStore) {
		this.productComboStore = productComboStore;
	}

	public String getProductComboCategory() {
		return productComboCategory;
	}

	public void setProductComboCategory(String productComboCategory) {
		this.productComboCategory = productComboCategory;
	}

	public String getProductComboInformation() {
		return productComboInformation;
	}

	public void setProductComboInformation(String productComboInformation) {
		this.productComboInformation = productComboInformation;
	}

	public int getProductComboPrice() {
		return productComboPrice;
	}

	public void setProductComboPrice(int productComboPrice) {
		this.productComboPrice = productComboPrice;
	}

	public int getProductComboStock() {
		return productComboStock;
	}

	public void setProductComboStock(int productComboStock) {
		this.productComboStock = productComboStock;
	}

	public int getProductComboCount() {
		return productComboCount;
	}

	public void setProductComboCount(int productComboCount) {
		this.productComboCount = productComboCount;
	}

	public int getProductComboIndex() {
		return productComboIndex;
	}

	public void setProductComboIndex(int productComboIndex) {
		this.productComboIndex = productComboIndex;
	}

	public int getProductComboContentCount() {
		return productComboContentCount;
	}

	public void setProductComboContentCount(int productComboContentCount) {
		this.productComboContentCount = productComboContentCount;
	}

	public int getProductComboADNumber() {
		return productComboADNumber;
	}

	public void setProductComboADNumber(int productComboADNumber) {
		this.productComboADNumber = productComboADNumber;
	}

	public long getTotalCountNumber() {
		return totalCountNumber;
	}

	public void setTotalCountNumber(long totalCountNumber) {
		this.totalCountNumber = totalCountNumber;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public int getProductComboDiscount() {
		return productComboDiscount;
	}

	public void setProductComboDiscount(int productComboDiscount) {
		this.productComboDiscount = productComboDiscount;
	}

	public int getProductComboDiscountedPrice() {
		return productComboDiscountedPrice;
	}

	public void setProductComboDiscountedPrice(int productComboDiscountedPrice) {
		this.productComboDiscountedPrice = productComboDiscountedPrice;
	}

	// 로그용 toString() 오버라이딩
	@Override
	public String toString() {
		return "ProductComboVO [productComboNumber=" + productComboNumber + ", productComboName=" + productComboName
				+ ", productComboImage=" + productComboImage + ", productComboStore=" + productComboStore
				+ ", productComboCategory=" + productComboCategory + ", productComboInformation="
				+ productComboInformation + ", productComboPrice=" + productComboPrice + ", productComboStock="
				+ productComboStock + ", productComboCount=" + productComboCount + ", productComboIndex="
				+ productComboIndex + ", productComboContentCount=" + productComboContentCount
				+ ", productComboADNumber=" + productComboADNumber + ", totalCountNumber=" + totalCountNumber
				+ ", condition=" + condition + ", searchKeyword=" + searchKeyword + ", productComboDiscount="
				+ productComboDiscount + ", productComboDiscountedPrice=" + productComboDiscountedPrice + "]";
	}
}