package zic.honeyComboFactory.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "upload")
// application.properties에서 설정값을 자동으로 매핑해주는 클래스
public class CKEditor5ImgUploadConfig {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}