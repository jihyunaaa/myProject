package zic.honeyComboFactory.common.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration // 설정 클래스임을 나타냄 (스프링이 자동으로 읽음)
@EnableTransactionManagement // 스프링에서 트랜잭션을 관리할 수 있도록 설정
@EnableAspectJAutoProxy // AOP 기능을 활성화
public class TransactionConfig {

    @Bean // PlatformTransactionManager를 스프링 빈으로 등록
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        // DataSourceTransactionManager는 데이터 소스와 연결되어 트랜잭션을 처리
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean // TransactionTemplate를 스프링 빈으로 등록
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}