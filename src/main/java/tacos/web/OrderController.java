package tacos.web;



import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import tacos.Order;
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
	
	@GetMapping("/current")
	public String orderForm(Model model) {
		model.addAttribute("order", new Order());
		return "orderForm";
	}
	
	
	@PostMapping
	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus) {
		if(errors.hasErrors()) {
			return "orderForm";
		}
		
		logger.info("Order submmited: " + order);
		
		orderRepo.save(order);            // 리포지토리의 save 메서드를 통해 폼에서 제출된 order 객체를 저장 
		sessionStatus.setComplete();      // 이전 주문 및 연관 타코가 세션에 남아있을 수 있기 때문에 , 세션을 재설정
		
		return "redirect:/";
	}
}
