package zic.honeyComboFactory.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration // 설정 클래스임을 나타냄 (스프링이 자동으로 읽음)
public class ViewResolverConfig {

    @Bean // InternalResourceViewResolver를 스프링 빈으로 등록
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        // ViewResolver가 JSP 파일을 찾을 때 사용할 접두어(prefix)
        resolver.setPrefix("/");

        // ViewResolver가 JSP 파일을 찾을 때 사용할 접미어(suffix)
        resolver.setSuffix(".jsp");

        return resolver;
    }
}