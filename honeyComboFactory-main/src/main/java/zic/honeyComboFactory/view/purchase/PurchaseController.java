package zic.honeyComboFactory.view.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.common.service.ProductService;

@Controller
public class PurchaseController { // 주문 컨트롤러

	@Autowired
    private ProductService productService;

	 // 재고 확인
    @PostMapping("/purchase/checkStock")
    @ResponseBody
    public String checkStock(@RequestParam String cartProductNumberDatas, HttpSession session) {
        boolean isStockAvailable = this.productService.checkStock(cartProductNumberDatas, session);
        return isStockAvailable ? "true" : "false";
    }
}
