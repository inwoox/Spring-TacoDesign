package tacos.messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import tacos.Order;

@Service
public class JmsOrderMessagingService implements OrderMessagingService {
	private JmsTemplate jms;
	
	@Autowired
	public JmsOrderMessagingService(JmsTemplate jms) {
		this.jms = jms;
	}
	
	// MessagePostProcess를 구현한 메서드
	private Message addOrderSource(Message message) throws JMSException {
	    message.setStringProperty("X_ORDER_SOURCE", "WEB");
	    return message;
	 }
	
	
	// 메시지 객체를 전송하기 전에, 마지막 매개변수로 MessagePostProcessor (addOrderSource 메서드) 를 전달하여
	// 전송하기 전에 메시지에 커스텀 헤더를 추가한다.
	@Override
	public void sendOrder(Order order) {
		jms.convertAndSend("tacocloud.order.queue", order, this::addOrderSource);
	}
	
	
	
	public void sendOrder1(Order order) {
		jms.convertAndSend("tacocloud.order.queue", order, message -> {
			message.setStringProperty("X_ORDER_SOURCE", "WEB");
			return message;
		});
	}
	
	public void sendOrder2(Order order) {
		jms.send("tacocloud.order.queue", session -> {
			Message message = session.createObjectMessage(order);
			message.setStringProperty("X_ORDER_SOURCE", "WEB");  // 온라인 주문 WEB을 나타내는 커스텀 헤더를 메시지에 추가
			return message;
		});
	}
	
	// 객체를 메시지로 변환하여 전달 (기본적으로 제공되는 SimpleMessageConverter가 사용된다. / 객체는 Serializable 인터페이스를 구현하는 객체여야한다)
	public void sendOrder3(Order order) {
		jms.convertAndSend("tacocloud.order.queue", order);
	}
	
	// Order 객체를 가지고 메시지를 만들어 전달 (도착지 지정)
	public void sendOrder4(Order order) {
		jms.send("tacocloud.order.queue", session -> session.createObjectMessage(order));
	}
	
	// Order 객체를 가지고 메시지를 만들어 전달 (도착지 미 지정) 
	// 기본 도착지 (큐 또는 토픽) 구성 속성으로 적용 필요 (spring.jms.template.default-destination) 
	public void sendOrder5(Order order) {
		jms.send(session -> session.createObjectMessage(order));
	}
	
	public void sendOrder6(Order order) {
		jms.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException { return session.createObjectMessage(order); }
		});
	}
}
