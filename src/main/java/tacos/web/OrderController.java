package tacos.web;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tacos.Order;

@Controller
@RequestMapping("/orders")
public class OrderController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/current")
	public String orderForm(Model model) {
		model.addAttribute("order", new Order());
		return "orderForm";
	}
	
	
	@PostMapping
	public String processOrder(Order order) {
		
		logger.info("Order submmited: " + order);
		return "redirect:/";
	}
}
