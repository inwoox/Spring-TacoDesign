

package tacos.messaging;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tacos.Order;

@Service
public class RabbitOrderMessagingService implements OrderMessagingService {
	
	private RabbitTemplate rabbit;
	
	@Autowired
	public RabbitOrderMessagingService(RabbitTemplate rabbit) {
		this.rabbit = rabbit;
	}
	
	// order 객체와, 라우팅 키만 인자로 가지고 전달, 즉 기본 거래소로 전달된다.
	@Override
	public void sendOrder(Order order) {
		rabbit.convertAndSend("tacocloud.order", order);
	}
	
	public void sendOrder1(Order order) {
		MessageConverter converter = rabbit.getMessageConverter();
		MessageProperties props = new MessageProperties();  
		props.setHeader("X_ORDER_SOURCE", "WEB");             // 전송하는 메시지에 헤더 (속성) 를 설정해야할 경우
		Message message = converter.toMessage(order, props);
		
		// 메시지와 함께 거래소 및 라우팅 키를 인자로 전달할 수 있다 / 여기서는 라우팅 키인 tacocloud.order만 인자로 전달하므로, 기본 거래소가 사용된다. 
		// 기본 거래소 이름은 "" / 기본 라우팅 키도 ""  /  이런 기본 값은 spring.rabbitmq.template.exchange와 routing-key 속성을 설정하여 변경할 수 있다.
		rabbit.send("tacocloud.order", message);             
	}
	
	// convertAndSend()를 사용할 때는 MessageProperties 객체를 직접 사용할 수 없으므로, MessagePostProcessor를 사용한다.
	public void sendOrder2(Order order) {
		rabbit.convertAndSend("tacocloud.order.queue", order, new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException{
				MessageProperties props = message.getMessageProperties();
				props.setHeader("X_ORDER_SOURCE","WEB");
				return message;
			}
		});
	}
	
}
