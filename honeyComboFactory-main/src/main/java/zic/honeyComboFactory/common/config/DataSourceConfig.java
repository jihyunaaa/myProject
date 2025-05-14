package zic.honeyComboFactory.common.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration // 설정 클래스임을 나타냄 (스프링이 자동으로 읽음)
public class DataSourceConfig {

    @Bean // DataSource 빈 등록
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        // Oracle JDBC 드라이버 설정
        config.setDriverClassName("oracle.jdbc.OracleDriver");

        // DB 접속 URL (localhost, xe 사용 중)
        config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");

        // DB 접속 계정
        config.setUsername("honeyComboFactory");
        config.setPassword("0000");

        // 커넥션 풀 성능 관련 설정
        config.setConnectionTestQuery("SELECT 1 FROM DUAL"); // 커넥션 유효성 검사 쿼리
        config.setMaximumPoolSize(10);       // 최대 커넥션 개수
        config.setMinimumIdle(5);            // 최소 유휴 커넥션 수
        config.setIdleTimeout(30000);        // 유휴 커넥션 최대 유지 시간 (ms)
        config.setConnectionTimeout(30000);  // 커넥션 가져올 때 최대 대기 시간 (ms)
        config.setMaxLifetime(1800000);      // 커넥션 최대 생존 시간 (ms)

        // 설정을 바탕으로 HikariDataSource 객체 생성
        return new HikariDataSource(config);
    }
}