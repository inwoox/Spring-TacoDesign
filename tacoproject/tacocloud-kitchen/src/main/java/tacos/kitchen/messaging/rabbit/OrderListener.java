
package tacos.kitchen.messaging.rabbit.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import tacos.Order;
import tacos.kitchen.KitchenUI;


@Profile("rabbitmq-listener")
@Component
public class OrderListener {
	private KitchenUI ui;
	
	@Autowired
	public OrderListener(KitchenUI ui) {
		this.ui = ui;
	}
	
	
	// RabbitListener는 JmsListener와 거의 동일하게 작동한다. / 다른 리스너를 사용하고 싶으면 , 리스너 애노테이션만 변경하면 된다.
	@RabbitListener(queues = "tacocloud.order.queue")
	public void receiveOrder(Order order) {
		ui.displayOrder(order);
	}
}
