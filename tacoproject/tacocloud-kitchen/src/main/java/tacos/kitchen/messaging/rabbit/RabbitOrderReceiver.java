
package tacos.kitchen.messaging.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.amqp.core.Message;

import tacos.Order;
import tacos.kitchen.OrderReceiver;

@Profile("rabbitmq-template")
@Component("templateOrderReceiver")
public class RabbitOrderReceiver implements OrderReceiver {
	private RabbitTemplate rabbit;
	private MessageConverter converter;
	
	@Autowired
	public RabbitOrderReceiver(RabbitTemplate rabbit) {
		this.rabbit = rabbit;
		this.converter = rabbit.getMessageConverter();
	}
	
	// 수신 및 변환을 한번에 한다.
	@Override
	public Order receiveOrder() {
		return (Order) rabbit.receiveAndConvert("tacocloud.order.queue");
	}
	
	// 캐스팅 대신, Order 객체를 직접 수신하게 한다. / Type-Safe 측면에서는 캐스팅보다 좋다. 
	// 단 이렇게 하려면, 메시지 변환기가 SmartMessageConverter 인터페이스를 구현한 클래스여야한다. (예 : Jackson2JsonMessageConverter)
	public Order receiveOrder1() {
		return rabbit.receiveAndConvert("tacocloud.order.queue", new ParameterizedTypeReference<Order>() {});
	}
	
	// receive 메서드를 호출해 tacocloud.orders 큐로부터 주문 데이터를 가져온다.
	// receive에 타임 아웃 값을 인자로 전달하지 않았기 때문에, 즉시 Message 객체 또는 null 값을 반환
	public Order receiveOrder2() {					
		Message message = rabbit.receive("tacocloud.orders");
		return message != null 
				? (Order) converter.fromMessage(message): null;
	}
	
	// 주문 데이터가 큐에 생길 때까지 30초간 기다리고 싶을 경우, 타임 아웃 값을 설정
	// 이렇게 하지 않고, 구성 속성 (spring.rabbitmq.template.receive-timeout) 설정 가능 
	// 클래스에 @ConfigurationProperties 불 필요)
	public Order receiveOrder3() {
		Message message = rabbit.receive("tacocloud.order.queue", 30000);
		return message != null
				? (Order) converter.fromMessage(message): null;
	}
	
}
