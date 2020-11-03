package tacos.web;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Component
@ConfigurationProperties(prefix="taco.orders") // 해당 빈의 속성의 값들을, 스프링 환경의 속성 (원천 속성: yml 등)에 정의된 구성 속성을 통해 주입할 수 있다.
																							 // 접두어를 붙였으므로, 구성 속성 값을 설정시 taco.orders.pageSize라는 이름을 사용한다.																	 
@Data
@Validated
public class OrderProps {                      // 이렇게 구성 속성 홀더 빈을 이용해, 구성 속성 관련 코드를 한군데에 모아둘 수 있다.
	
	@Min(value=5, message="must be between 5 and 25")
	@Max(value=25, message="must be between 5 and 25")
	private int pageSize = 20;
}

// @ConfigurationProperties
// 이 애노테이션이 붙은 클래스에 관한 메타데이터를 별도로 설정할 수 있다 (spring-boot-configuration-processor 의존성 추가 / additional-spring-configuration-metadata.json 설정) 
