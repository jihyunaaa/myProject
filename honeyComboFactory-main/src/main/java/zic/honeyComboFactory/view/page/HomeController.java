package zic.honeyComboFactory.view.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController { // 인덱스 페이지 이동 컨트롤러
	// 인덱스 페이지 이동
	@GetMapping("/")
	public String home() {
		return "index"; // -> /index.jsp로 이동
	}
}
