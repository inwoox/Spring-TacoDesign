
package tacos.ingredientclient.rest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;


@Configuration
@Conditional(NotFeignAndNotWebClientCondition.class)
@Slf4j
public class RestTemplateConfig {

	// 유레카 클라이언트 의존성을 추가하면 로드밸런싱된 RestTemplate 빈을 선언할 수 있다.
	// 이는 두가지 목적을 가지는데, 첫째로 RestTemplate이 리본을 통해서만 서비스를 찾는다는 것을 스프링 클라우드에 알린다.
	// 두번째로 주입 식별자로 동작하게 한다. (주입 식별자는 서비스 이름이며, 호스트와 포트 대신 사용한다)
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
