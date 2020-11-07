

package tacos.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tacos.Order;



// RabbitMQ가 거래소와 큐를 사용하여 메시지를 처리하는 반면,
// 카프카는 토픽만 사용한다.

// 토픽은 클러스터의 모든 브로커에 걸쳐 복제 (클러스터는 여러 개의 브로커로 구성)
// 각 토픽은 여러 개의 파티션으로 분할될 수 있다.


@Service
public class KafkaOrderMessagingService implements OrderMessagingService {
	
	private KafkaTemplate<String, Order> kafkaTemplate;
	
	@Autowired
	public KafkaOrderMessagingService(KafkaTemplate<String,Order> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	// 카프카는 convertAndSend() 메서드가 없다.  제네릭 타입을 사용하고, 메시지를 전송할 때 직접 도메인 타입을 처리할 수 있기 때문에
	// 따라서 모든 send() 메서드가 convertAndSend()의 기능을 갖고 있다고 생각할 수 있다.
	
	
	// 매개변수 - 메시지가 전송될 토픽 (필수), 토픽 데이터를 쓰는 파티션 (선택), 레코드 전송 키 (선택), 타임스탬프 (선택), 페이로드 (예 : Order 객체) (필수)
	@Override
	public void sendOrder(Order order) {
		kafkaTemplate.send("tacocloud.orders.topic", order);
	}
	
	// 구성 속성으로, 기본 토픽을 설정하면, 이렇게 토픽 이름을 생략하고 전송할 수 있다.
	public void sendOrder1(Order order) {
		kafkaTemplate.sendDefault(order);
	}
}
