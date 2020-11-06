
package tacos.kitchen.messaging.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import tacos.Order;
import tacos.kitchen.KitchenUI;


// 메시지 리스너는 중단 없이 다수의 메시지를 빠르게 처리할 수 있지만, 주문만큼 처리 속도가 따라가지 못해 병목 현상이 발생할 수 있다
// 주방 직원에게 과부하가 걸리지 않도록, 주방의 사용자 인터페이스는 도착하는 주문을 버퍼링해야한다.

@Profile("jms-listener")
@Component
public class OrderListener {
	private KitchenUI ui;
	
	@Autowired
	public OrderListener(KitchenUI ui) {
		this.ui = ui;
	}
	
	@JmsListener(destination= "tacocloud.order.queue")  // 도착지의 메시지를 리스닝하기 위해 애노테이션 지정 / 스프링의 프레임워크 코드를 통해, 
	public void receiveOrder(Order order) {				// 도착지에 메시지가 도착하면, 메시지의 Order 객체가 인자로 전달되면서 이 메서드가 자동 호출
		ui.displayOrder(order);							
	}
}
