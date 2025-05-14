package zic.honeyComboFactory.biz.purchaseDetailVO;

public class PurchaseDetailVO { // PurchaseDetailDTO
	// 테이블 컬럼
	private long purchaseDetailNumber;
	private long purchaseProductCount;
	private long productSingleNumber;
	private long productComboNumber;
	private long purchaseNumber;
	// 추가 멤버변수
	private long memberNumber;
	private String productSingleName;
	private String productComboName;
	private long productSinglePrice;
	private long productComboPrice;
	private long purchaseTotalPrice;
	private String condition;
	// 최프때 추가된 멤버변수
	private int productSingleDiscount;
	private int productSingleDiscountedPrice;
	private int productComboDiscount;
	private int productComboDiscountedPrice;

	// getter, setter
	public long getPurchaseDetailNumber() {
		return purchaseDetailNumber;
	}

	public int getProductSingleDiscountedPrice() {
		return productSingleDiscountedPrice;
	}

	public void setProductSingleDiscountedPrice(int productSingleDiscountedPrice) {
		this.productSingleDiscountedPrice = productSingleDiscountedPrice;
	}

	public int getProductComboDiscountedPrice() {
		return productComboDiscountedPrice;
	}

	public void setProductComboDiscountedPrice(int productComboDiscountedPrice) {
		this.productComboDiscountedPrice = productComboDiscountedPrice;
	}

	public int getProductSingleDiscount() {
		return productSingleDiscount;
	}

	public void setProductSingleDiscount(int productSingleDiscount) {
		this.productSingleDiscount = productSingleDiscount;
	}

	public int getProductComboDiscount() {
		return productComboDiscount;
	}

	public void setProductComboDiscount(int productComboDiscount) {
		this.productComboDiscount = productComboDiscount;
	}

	public void setPurchaseDetailNumber(long purchaseDetailNumber) {
		this.purchaseDetailNumber = purchaseDetailNumber;
	}

	public long getPurchaseProductCount() {
		return purchaseProductCount;
	}

	public void setPurchaseProductCount(long purchaseProductCount) {
		this.purchaseProductCount = purchaseProductCount;
	}

	public long getProductSingleNumber() {
		return productSingleNumber;
	}

	public void setProductSingleNumber(long productSingleNumber) {
		this.productSingleNumber = productSingleNumber;
	}

	public long getProductComboNumber() {
		return productComboNumber;
	}

	public void setProductComboNumber(long productComboNumber) {
		this.productComboNumber = productComboNumber;
	}

	public long getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(long purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public long getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(long memberNumber) {
		this.memberNumber = memberNumber;
	}

	public String getProductSingleName() {
		return productSingleName;
	}

	public void setProductSingleName(String productSingleName) {
		this.productSingleName = productSingleName;
	}

	public String getProductComboName() {
		return productComboName;
	}

	public void setProductComboName(String productComboName) {
		this.productComboName = productComboName;
	}

	public long getProductSinglePrice() {
		return productSinglePrice;
	}

	public void setProductSinglePrice(long productSinglePrice) {
		this.productSinglePrice = productSinglePrice;
	}

	public long getProductComboPrice() {
		return productComboPrice;
	}

	public void setProductComboPrice(long productComboPrice) {
		this.productComboPrice = productComboPrice;
	}

	public long getPurchaseTotalPrice() {
		return purchaseTotalPrice;
	}

	public void setPurchaseTotalPrice(long purchaseTotalPrice) {
		this.purchaseTotalPrice = purchaseTotalPrice;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	// 로그용 toString() 오버라이딩
	@Override
	public String toString() {
		return "PurchaseDetailVO [purchaseDetailNumber=" + purchaseDetailNumber + ", purchaseProductCount="
				+ purchaseProductCount + ", productSingleNumber=" + productSingleNumber + ", productComboNumber="
				+ productComboNumber + ", purchaseNumber=" + purchaseNumber + ", memberNumber=" + memberNumber
				+ ", productSingleName=" + productSingleName + ", productComboName=" + productComboName
				+ ", productSinglePrice=" + productSinglePrice + ", productComboPrice=" + productComboPrice
				+ ", purchaseTotalPrice=" + purchaseTotalPrice + ", condition=" + condition + ", productSingleDiscount="
				+ productSingleDiscount + ", productSingleDiscountedPrice=" + productSingleDiscountedPrice
				+ ", productComboDiscount=" + productComboDiscount + ", productComboDiscountedPrice="
				+ productComboDiscountedPrice + "]";
	}
	
}