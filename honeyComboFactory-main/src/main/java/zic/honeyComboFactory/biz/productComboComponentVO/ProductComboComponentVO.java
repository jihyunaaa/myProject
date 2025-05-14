package zic.honeyComboFactory.biz.productComboComponentVO;

public class ProductComboComponentVO { // ProductComboComponentDTO
	// 테이블 컬럼
	private long productComboComponentNumber;
	private long productComboNumber;
	private long productComboComponentOne;
	private long productComboComponentTwo;
	private long productComboComponentThree;
	// 추가 멤버변수
	private long productSingleNumber;
	private long totalCountNumber;
	private String productSingleImage;
	private String productSingleName;
	private long productSinglePrice;
	private int productComboComponentIndex;
	private int productComboComponentContentCount;
	private String condition;
	private String searchKeyword;
	// 최프 때 추가된 멤버변수
	private long productSingleDiscountedPrice;

	// getter, setter

	public long getProductComboComponentNumber() {
		return productComboComponentNumber;
	}

	public long getProductSingleDiscountedPrice() {
		return productSingleDiscountedPrice;
	}

	public void setProductSingleDiscountedPrice(long productSingleDiscountedPrice) {
		this.productSingleDiscountedPrice = productSingleDiscountedPrice;
	}

	public void setProductComboComponentNumber(long productComboComponentNumber) {
		this.productComboComponentNumber = productComboComponentNumber;
	}

	public long getProductComboNumber() {
		return productComboNumber;
	}

	public void setProductComboNumber(long productComboNumber) {
		this.productComboNumber = productComboNumber;
	}

	public long getProductComboComponentOne() {
		return productComboComponentOne;
	}

	public void setProductComboComponentOne(long productComboComponentOne) {
		this.productComboComponentOne = productComboComponentOne;
	}

	public long getProductComboComponentTwo() {
		return productComboComponentTwo;
	}

	public void setProductComboComponentTwo(long productComboComponentTwo) {
		this.productComboComponentTwo = productComboComponentTwo;
	}

	public long getProductComboComponentThree() {
		return productComboComponentThree;
	}

	public void setProductComboComponentThree(long productComboComponentThree) {
		this.productComboComponentThree = productComboComponentThree;
	}

	public long getTotalCountNumber() {
		return totalCountNumber;
	}

	public void setTotalCountNumber(long totalCountNumber) {
		this.totalCountNumber = totalCountNumber;
	}

	public String getProductSingleImage() {
		return productSingleImage;
	}

	public void setProductSingleImage(String productSingleImage) {
		this.productSingleImage = productSingleImage;
	}

	public String getProductSingleName() {
		return productSingleName;
	}

	public void setProductSingleName(String productSingleName) {
		this.productSingleName = productSingleName;
	}

	public long getProductSinglePrice() {
		return productSinglePrice;
	}

	public void setProductSinglePrice(long productSinglePrice) {
		this.productSinglePrice = productSinglePrice;
	}

	public int getProductComboComponentIndex() {
		return productComboComponentIndex;
	}

	public void setProductComboComponentIndex(int productComboComponentIndex) {
		this.productComboComponentIndex = productComboComponentIndex;
	}

	public int getProductComboComponentContentCount() {
		return productComboComponentContentCount;
	}

	public void setProductComboComponentContentCount(int productComboComponentContentCount) {
		this.productComboComponentContentCount = productComboComponentContentCount;
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

	public long getProductSingleNumber() {
		return productSingleNumber;
	}

	public void setProductSingleNumber(long productSingleNumber) {
		this.productSingleNumber = productSingleNumber;
	}

	// 로그용 toString() 오버라이딩
	@Override
	public String toString() {
		return "ProductComboComponentVO [productComboComponentNumber=" + productComboComponentNumber
				+ ", productComboNumber=" + productComboNumber + ", productComboComponentOne="
				+ productComboComponentOne + ", productComboComponentTwo=" + productComboComponentTwo
				+ ", productComboComponentThree=" + productComboComponentThree + ", totalCountNumber="
				+ totalCountNumber + ", productSingleImage=" + productSingleImage + ", productSingleNumber="
				+ productSingleNumber + ", productSingleName=" + productSingleName + ", productSinglePrice="
				+ productSinglePrice + ", productComboComponentIndex=" + productComboComponentIndex
				+ ", productComboComponentContentCount=" + productComboComponentContentCount + ", condition="
				+ condition + ", searchKeyword=" + searchKeyword + ", productSingleDiscountedPrice="
				+ productSingleDiscountedPrice + "]";
	}
}