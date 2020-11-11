package tacos.email;

import java.util.Map;

import org.springframework.integration.handler.GenericHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


//	handle 메서드에서 입력된 Order 객체를 받으며, 주입된 RestTemplate를 이용해 주문을 제출한다.
//	이 핸들러가 플로우의 제일 끝이라는 것을 나타내기 위해 handle 메서드가 null을 반환한다.
@Component
public class OrderSubmitMessageHandler {
	private RestTemplate rest;
	private ApiProperties apiProps;
	
	public OrderSubmitMessageHandler(ApiProperties apiProps, RestTemplate rest) {
		this.apiProps = apiProps;
		this.rest = rest;
	}
	
	//	RestTemplate을 사용하기 위해 spring-boot-starter-web 의존성을 추가한다.
	public Object handle(Order order, Map<String, Object> headers) {
		rest.postForObject(apiProps.getUrl(), order, String.class);
		return null;
	}
}