package zic.honeyComboFactory.biz.common.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrawlingListener {

    @Autowired
    private GsCrawlingMybatisService gsCrawlingService;

    @Autowired
    private CuCrawlingMybatisService cuCrawlingService;

   // @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("[리스너] 서버 시작됨 - 크롤링 시작");
        gsCrawlingService.runCrawling();
        cuCrawlingService.runCrawling();
    }
}
