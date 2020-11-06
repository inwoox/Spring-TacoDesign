
package tacos.messaging;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import tacos.Order;

@Configuration
public class MessagingConfig {
	
	// 기본적으로 SimpleMessageConverter가 사용되지만, Serializable 인터페이스를 구현하는 객체여야하는 제약을 피하기 위해
	// MappingJackson2MessageConverter를 사용할 수 있다.
	
	// 메시지의 변환 타입 (원래 객체의 타입)을 수신자가 알아야하기 때문에, 변환 타입 정보를 설정하고, 컨버터 인스턴스를 반환한다.
	// 반환 받은 컨버터 인스턴스를 가지고, 빈을 생성하므로, 이후에는 이 컨버터를 사용하게 된다.
	@Bean
	public MappingJackson2MessageConverter messageConverter() {
		MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
		
		// 수신된 메시지의 변환 타입을 메시지 수신자가 알아야한다
		// 여기에는 반환 타입의 클래스 이름이 포함 된다.
		messageConverter.setTypeIdPropertyName("_typeId"); 
		
		// 하지만 유연성이 떨어지므로 setTypeIdMappings()를 호출하여 실제 타입에 임의의 타입 이름을 매핑시킨다.
		// 이렇게하면 _typeId 속성에 전송되는 클래스 이름 (패키지 전체 경로 포함) 대신 order 값이 전송된다.
		Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
		typeIdMappings.put("order", Order.class);
		messageConverter.setTypeIdMappings(typeIdMappings);
		
		return messageConverter;
	}
}
