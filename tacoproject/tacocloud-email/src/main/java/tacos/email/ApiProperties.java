package tacos.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


//	URL의 하드코딩을 피하기 위해 postForObject() 호출에 사용될, 구성 속성을 설정할 수 있게 하는 클래스

@Data
@ConfigurationProperties(prefix="tacocloud.api")
@Component
public class ApiProperties {
	private String url;
}