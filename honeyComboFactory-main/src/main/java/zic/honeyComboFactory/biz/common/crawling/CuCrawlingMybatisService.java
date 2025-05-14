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
public class CuCrawlingMybatisService {

    @Autowired
    private OracleProductSingleDAOMybatis productSingleDAO;

    // 공통 VO 생성 메서드
    //크롤링한 상품 정보를 vo객체로 변환
    private ProductSingleVO createProductVO(String name, int price, String img, String category, String condition) {
        ProductSingleVO vo = new ProductSingleVO();
        vo.setProductSingleStore("CU");
        vo.setProductSingleName(name);
        vo.setProductSinglePrice(price);
        vo.setProductSingleStock(100);
        vo.setProductSingleImage(img);
        vo.setProductSingleCategory(category);
        vo.setCondition(condition);
        return vo;
    }

    public void runCrawling() {
        System.out.println("[CU] 크롤링 시작");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0");
        WebDriver driver = new ChromeDriver(options);//크롬드라이버 생성
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            crawlHotIssue(driver, wait);
            crawlEvent(driver, wait);
            crawlCategory(driver, wait);
        } catch (Exception e) {
            System.out.println("[CU] 크롤링 중 예외 발생");
            e.printStackTrace();
        } finally {
            driver.quit();
            System.out.println("[CU] 크롬 드라이버 종료");
        }
    }

    // 핫이슈 (NEW 뱃지 포함 상품)
    private void crawlHotIssue(WebDriver driver, WebDriverWait wait) throws Exception {
        System.out.println("[CU/HOTISSUE] 크롤링 시작");
        driver.get("https://cu.bgfretail.com/product/product.do?category=product&depth2=4&sf=N");

        Thread.sleep(2000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.prod_list")));

        List<WebElement> items = driver.findElements(By.cssSelector("li.prod_list"));

        for (WebElement item : items) {
            try {
                if (item.findElements(By.cssSelector(".tag span.new")).isEmpty()) continue;

                String name = item.findElement(By.className("name")).getText().trim();
                if (name.isEmpty()) continue;

                String img = item.findElement(By.tagName("img")).getAttribute("src");

                String priceText = item.findElement(By.cssSelector(".price strong")).getText().replaceAll("[^0-9]", "");
                int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);

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

    // 1+1 증정상품
    private void crawlEvent(WebDriver driver, WebDriverWait wait) throws Exception {
        System.out.println("[CU/PLUSPRODUCT] 크롤링 시작");
        driver.get("https://cu.bgfretail.com/event/plus.do?category=event&depth2=1&sf=N");

        Thread.sleep(2000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.prod_list")));

        List<WebElement> items = driver.findElements(By.cssSelector("li.prod_list"));

        for (WebElement item : items) {
            try {
                String badgeText = item.findElement(By.className("badge")).getText().trim();
                if (!badgeText.contains("1+1")) continue;

                String name = item.findElement(By.className("name")).getText().trim();
                if (name.isEmpty()) continue;

                String img = item.findElement(By.tagName("img")).getAttribute("src");

                String priceText = item.findElement(By.cssSelector(".price strong")).getText().replaceAll("[^0-9]", "");
                int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);

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

    // 식품, 음료, 생활용품
    private void crawlCategory(WebDriver driver, WebDriverWait wait) throws Exception {
        String[] urls = {
            "https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=1",
            "https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=6",
            "https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=7"
        };
        String[] categories = { "FOODPRODUCT", "BEVERAGEPRODUCT", "DAILYSUPPLIESPRODUCT" };

        for (int i = 0; i < urls.length; i++) {
            System.out.println("[CU/" + categories[i] + "] 카테고리 크롤링 시작");

            driver.get(urls[i]);
            Thread.sleep(2000);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.prod_list")));

            List<WebElement> items = driver.findElements(By.cssSelector("li.prod_list"));
            System.out.println("[CU/" + categories[i] + "] 상품 수: " + items.size());

            for (WebElement item : items) {
                try {
                    String name = item.findElement(By.className("name")).getText().trim();
                    if (name.isEmpty()) continue;

                    String img = item.findElement(By.tagName("img")).getAttribute("src");

                    String priceText = item.findElement(By.cssSelector(".price strong")).getText().replaceAll("[^0-9]", "");
                    int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);

                    ProductSingleVO vo = createProductVO(name, price, img, categories[i], "SELECTALLCATEGORY");

                    boolean inserted = productSingleDAO.insert(vo);
                    if (inserted) {
                        System.out.println("[CATEGORY][" + categories[i] + "][INSERT] " + name + " / " + price + "원");
                    } else {
                        System.out.println("[CATEGORY][" + categories[i] + "][SKIP] 중복 상품: " + name);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
