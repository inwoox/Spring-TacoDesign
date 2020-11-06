package tacos.kitchen.messaging.jms;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import tacos.Order;
import tacos.kitchen.OrderReceiver;


// 음식 조리사가 타코를 만들 준비가 되었다는 것을 나타내기 위해 , 버튼을 누르거나 어떤 액션을 취하면
// receiveOrder()가 호출되고, 주문 메시지가 수신될 때까지는 아무일도 생기지 않는다.  (풀 모델의 형태)

// 주문 메시지가 수신되면 receiveOrder로부터 반환되고, 이 정보는 조리사가 일을 하도록 주문 명세를 보여주는데 사용된다.

@Profile("jms-template")
@Component("templateOrderReceiver")
public class JmsOrderReceiver implements OrderReceiver{
	private JmsTemplate jms;
	private MessageConverter converter;
	
	@Autowired
	public JmsOrderReceiver(JmsTemplate jms, MessageConverter converter) {
		this.jms = jms;
		this.converter = converter;
	}
	
	// 메타 데이터가 필요 없이, 페이로드(예 : Order 객체)만 필요할 경우에는 receiveAndConvert()를 사용하는 것이 간단하다.
	// 이 경우에는 컨버터를 주입할 필요가 없다. (변환 및 수신이 한번에 수행되기 때문에)
	public Order receiveOrder() {
		return (Order) jms.receiveAndConvert("tacocloud.order.queue");
	}
	
	// 메시지를 수신하고, Order 객체로 변환 (메시지의 속성과 헤더를 살펴봐야할 때는 원시 Message 객체를 메시지로 수신하는게 유용할 수 있다)
	public Order receiveOrder1() {
		Message message = jms.receive("tacocloud.order.queue");
		return (Order) converter.fromMessage(message);
	}
	
	
}
