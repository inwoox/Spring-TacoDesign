package tacos.web;



import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import tacos.Order;
import tacos.User;
import tacos.data.OrderRepository;

@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private OrderRepository orderRepo; 
	public OrderController(OrderRepository orderRepo) {  // 리포지토리 주입
		this.orderRepo = orderRepo;
	}
	
	@GetMapping("/current")   // 인증된 사용자 user 객체를 메서드 인자로 받아, 사용자 정보를 Order 객체의 각 속성에 설정한다. (주문 폼 미리 채우기)
	public String orderForm(@AuthenticationPrincipal User user, @ModelAttribute Order order) {
		if(order.getDeliveryName() == null) { order.setDeliveryName(user.getFullname()); }
		if(order.getDeliveryStreet() == null) { order.setDeliveryStreet(user.getState()); }
		if(order.getDeliveryCity() == null) { order.setDeliveryCity(user.getCity()); }
		if(order.getDeliveryState() == null) { order.setDeliveryState(user.getState()); }
		if(order.getDeliveryZip() == null) { order.setDeliveryZip(user.getZip()); }
		
		return "orderForm";
	}
	
	
	@PostMapping	                 
	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user) {
		if(errors.hasErrors()) {
			return "orderForm";
		}
		
		order.setUser(user);              // 주문을 한 사용자가 누구인지 결정하는 방법으로 @AuthenticationPrincipal을 사용한다.
		logger.info("Order submmited: " + order);
		
		orderRepo.save(order);            // 리포지토리의 save 메서드를 통해 폼에서 제출된 order 객체를 저장 
		sessionStatus.setComplete();      // 이전 주문 및 연관 타코가 세션에 남아있을 수 있기 때문에 , 세션을 재설정
		
		return "redirect:/";
	}
}
