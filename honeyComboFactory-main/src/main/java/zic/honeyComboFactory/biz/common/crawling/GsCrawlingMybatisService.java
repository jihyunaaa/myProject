package zic.honeyComboFactory.biz.common.crawling;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.productSingle.impl.OracleProductSingleDAOMybatis;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;

@Service
public class GsCrawlingMybatisService {

    @Autowired
    private OracleProductSingleDAOMybatis productSingleDAO;

    /*
     * 상품 정보를 기반으로 ProductSingleVO 객체 생성
     * @param name 상품명
     * @param price 가격
     * @param img 이미지 경로
     * @param category 상품 카테고리 
     * @param condition DAO 조건 
     * @return 완성된 ProductSingleVO
     */
    private ProductSingleVO createProductVO(String name, int price, String img, String category, String condition) {
        ProductSingleVO vo = new ProductSingleVO();
        vo.setProductSingleStore("GS25");  // 상품브랜드
        vo.setProductSingleName(name);     // 상품명
        vo.setProductSinglePrice(price);   // 상품 가격
        vo.setProductSingleStock(100);     // 기본 재고
        vo.setProductSingleImage(img);     // 이미지 URL
        vo.setProductSingleCategory(category); // 상품 카테고리
        vo.setCondition(condition);        // DAO 조회 조건용
        return vo;
    }

    public void runCrawling() {
        System.out.println("[GS25] 크롤링 시작");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            crawlEvent(driver, wait);      // 1+1 상품
            crawlHotIssue(driver, wait);   // 신상품
            crawlCategory(driver, wait);   // 식품/음료/생활용품
        } catch (Exception e) {
            System.out.println("[GS25] 크롤링 중 예외 발생");
            e.printStackTrace();
        } finally {
            driver.quit();
            System.out.println("[GS25] 크롬 드라이버 종료");
        }
    }

    // 1+1 증정상품
    private void crawlEvent(WebDriver driver, WebDriverWait wait) throws Exception {
        System.out.println("[GS25/+1] 1+1 크롤링 시작");
        driver.get("https://gs25.gsretail.com/gscvs/ko/products/event-goods");

        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), '1+1')]")));
        tab.click();
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.prod_list li")));

        List<WebElement> items = driver.findElements(By.cssSelector("ul.prod_list li"));
        System.out.println("[GS25/+1] 상품 수: " + items.size());

        for (WebElement item : items) {
            try {
                String name = item.findElement(By.className("tit")).getText().trim();
                if (name.isEmpty()) continue;
                
                if (name.contains("노가리먹태")) {//상품명에 노가리 먹태가 포함되면 저장하지 않기
                    System.out.println("[SKIP] 노가리 상품 제외: " + name);
                    continue;
                }
                
                String img = item.findElement(By.className("prod_box"))
                                 .findElement(By.className("img"))
                                 .findElement(By.tagName("img"))
                                 .getAttribute("src");

                String priceText = item.findElement(By.className("price")).getText().replaceAll("[^0-9]", "");
                int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);

                // 모듈화된 메서드 사용
                ProductSingleVO vo = createProductVO(name, price, img, "PLUSPRODUCT", "SELECTALLCATEGORY");

                boolean insert = productSingleDAO.insert(vo);
                if (insert) {
                    System.out.println("[PLUSPRODUCT][INSERT] " + name + " / " + price + "원");
                } else {
                    System.out.println("[PLUSPRODUCT][SKIP] 중복 상품: " + name);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     // HOTISSUE (NEW)
    private void crawlHotIssue(WebDriver driver, WebDriverWait wait) throws Exception {
        System.out.println("[GS25/HOTISSUE] 핫이슈 크롤링 시작");
        driver.get("http://gs25.gsretail.com/gscvs/ko/products/youus-freshfood");
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.prod_list li")));

        List<WebElement> items = driver.findElements(By.cssSelector("ul.prod_list li"));
        System.out.println("[GS25/HOTISSUE] 전체 상품 수: " + items.size());

        for (WebElement item : items) {
            try {
                WebElement flag = item.findElement(By.cssSelector("p.flg04 span"));
                if (!flag.getText().contains("NEW")) continue;

                String name = item.findElement(By.className("tit")).getText().trim();
                if (name.isEmpty()) continue;

                String img = item.findElement(By.className("prod_box"))
                                 .findElement(By.className("img"))
                                 .findElement(By.tagName("img"))
                                 .getAttribute("src");

                String priceText = item.findElement(By.className("price")).getText().replaceAll("[^0-9]", "");
                int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);

                // 모듈화된 메서드 사용
                ProductSingleVO vo = createProductVO(name, price, img, "HOTISSUE", "SELECTALLCATEGORY");

                boolean insert = productSingleDAO.insert(vo);
                if (insert) {
                    System.out.println("[HOTISSUE][INSERT] " + name + " / " + price + "원");
                } else {
                    System.out.println("[HOTISSUE][SKIP] 중복 상품: " + name);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 식품/음료/생활용품
    private void crawlCategory(WebDriver driver, WebDriverWait wait) throws Exception {
        driver.get("https://gs25.gsretail.com/gscvs/ko/products/youus-different-service");

        String[] tabIds = { "productRamen", "productDrink", "productGoods" };
        String[] categoryValues = { "FOODPRODUCT", "BEVERAGEPRODUCT", "DAILYSUPPLIESPRODUCT" };

        for (int i = 0; i < tabIds.length; i++) {
            System.out.println("[GS25/" + categoryValues[i] + "] 탭 클릭");

            WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.id(tabIds[i])));
            tab.click();
            Thread.sleep(1000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.prod_list li")));

            List<WebElement> items = driver.findElements(By.cssSelector("ul.prod_list li"));
            System.out.println("[GS25/" + categoryValues[i] + "] 상품 수: " + items.size());

            for (WebElement item : items) {
                try {
                    String name = item.findElement(By.className("tit")).getText().trim();
                    if (name.isEmpty()) continue;

                    String img = item.findElement(By.className("prod_box"))
                                     .findElement(By.className("img"))
                                     .findElement(By.tagName("img"))
                                     .getAttribute("src");

                    String priceText = item.findElement(By.className("price")).getText().replaceAll("[^0-9]", "");
                    int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);

                    // 모듈화된 메서드 사용
                    ProductSingleVO vo = createProductVO(name, price, img, categoryValues[i], "SELECTALLCATEGORY");

                    boolean insert = productSingleDAO.insert(vo);
                    if (insert) {
                        System.out.println("[CATEGORY][" + categoryValues[i] + "][INSERT] " + name + " / " + price + "원");
                    } else {
                        System.out.println("[CATEGORY][" + categoryValues[i] + "][SKIP] 중복 상품: " + name);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
