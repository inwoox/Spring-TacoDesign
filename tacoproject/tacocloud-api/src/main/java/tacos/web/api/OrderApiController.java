package tacos.web.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tacos.Order;
import tacos.data.OrderRepository;
import tacos.messaging.OrderMessagingService;



//@Slf4j
//@Controller
//@RequestMapping("/orders")
//@SessionAttributes("order")    // 다수의 요청에서 계속 사용하기 위해 , 세션에 order 객체를 보존한다.

@RestController
@RequestMapping(path="/orders", produces="application/json")
@CrossOrigin(origins="*")
public class OrderApiController {              
	
	private OrderRepository orderRepo;
	private OrderMessagingService orderMessages;
	private EmailOrderService emailOrderService;
	
	public OrderApiController(OrderRepository repo, OrderMessagingService orderMessages, EmailOrderService emailOrderService) 
	{
		this.orderRepo = repo;
		this.orderMessages = orderMessages;
		this.emailOrderService = emailOrderService;
	}
	
	// 리포지토리에서 모든 주문 가져오기
	@GetMapping(produces="application/json")
	  public Iterable<Order> allOrders() {
	    return orderRepo.findAll();
	  }
	
	// 리포지토리에 주문 저장하기
	@PostMapping(consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Order postOrder(@RequestBody Order order) {
	  //orderMessages.sendOrder(order);  // 메시징으로 통신
	  return orderRepo.save(order);
	}
	
	// 이메일 주문을 Order로 파싱하여 저장
	@PostMapping(path="fromEmail", consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Order postOrderFromEmail(@RequestBody EmailOrder emailOrder) {
	  Order order = emailOrderService.convertEmailOrderToDomainOrder(emailOrder);
	  orderMessages.sendOrder(order);
	  return orderRepo.save(order);
	}
	
	
	// 주문 데이터 (전체 속성만) 저장
	@PutMapping("/{orderId}")
	public Order putOrder(@RequestBody Order order) {  // 요청 몸체의 데이터가 order에 바인딩 된다.
		return orderRepo.save(order);				   // 해당 주문의 속성이 생략되면, 생략된 속성의 값은 null이 되므로, 일부 데이터만 변경하더라도, 전체 데이터를 제출해야한다.
	}												   // 그래서 일부 데이터만 변경시에는 PATCH 요청을 사용한다.
	
	// 주문 데이터 (일부 속성 가능) 저장
	@PatchMapping(path="/{orderId}", consumes="application/json") // Patch는 부분 변경의 의미를 내포하고 있다.
	public Order patchOrder(@PathVariable("orderId") Long orderId, @RequestBody Order reqOrder) {
		Order order = orderRepo.findById(orderId).get();
		
		if(reqOrder.getDeliveryName() != null) order.setDeliveryName(reqOrder.getDeliveryName());       // 오더의 속성 값이 null이 아니면, 속성 값을 변경한다.
		if(reqOrder.getDeliveryStreet() != null) order.setDeliveryStreet(reqOrder.getDeliveryStreet()); // 이렇게하면 클라이언트에서 변경할 속성만 전송하면 된다.
		if(reqOrder.getDeliveryCity() != null) order.setDeliveryCity(reqOrder.getDeliveryCity());
		if(reqOrder.getDeliveryState() != null) order.setDeliveryState(reqOrder.getDeliveryState());
		if(reqOrder.getDeliveryZip() != null) order.setDeliveryZip(reqOrder.getDeliveryZip());
		if(reqOrder.getCcNumber() != null) order.setCcNumber(reqOrder.getCcNumber());
		if(reqOrder.getCcExpiration() != null) order.setCcExpiration(reqOrder.getCcExpiration());
		if(reqOrder.getCcCVV() != null) order.setCcCVV(reqOrder.getCcCVV());
		
		return orderRepo.save(order);
	}
	
	// 주문 데이터 삭제
	@DeleteMapping(path="/{orderId}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)  // 반환 데이터가 없다는 것을 클라이언트가 알 수 있도록 HTTP 상태 코드 사용
	public void deleteOrder(@PathVariable("orderId") Long orderId) {
		try {
			orderRepo.deleteById(orderId);
		}
		catch (EmptyResultDataAccessException e) {}
	}
	
// 스프링으로 폼 구현
	
//	private OrderProps props;
//	private OrderRepository orderRepo; 
//	
//	public OrderController(OrderRepository orderRepo , OrderProps props) {  // 리포지토리 주입
//		this.orderRepo = orderRepo;
//		this.props = props;
//	}
//	
//	@GetMapping("/current")   // 인증된 사용자 user 객체를 메서드 인자로 받아, 사용자 정보를 Order 객체의 각 속성에 설정한다. (주문 폼 미리 채우기)
//	public String orderForm(@AuthenticationPrincipal User user, @ModelAttribute Order order) {
//		if(order.getDeliveryName() == null) { 
//			order.setDeliveryName(user.getFullname()); 
//		}
//		if(order.getDeliveryStreet() == null) { 
//			order.setDeliveryStreet(user.getState()); 
//		}
//		if(order.getDeliveryCity() == null) { 
//			order.setDeliveryCity(user.getCity()); 
//		}
//		if(order.getDeliveryState() == null) { 
//			order.setDeliveryState(user.getState()); 
//		}
//		if(order.getDeliveryZip() == null) { 
//			order.setDeliveryZip(user.getZip()); 
//		}
//		return "orderForm";
//	}
//	
//	@PostMapping	                 
//	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user) {
//		if(errors.hasErrors()) {
//			return "orderForm";
//		}
//		
//		order.setUser(user);              // 주문을 한 사용자가 누구인지 결정하는 방법으로 @AuthenticationPrincipal을 사용한다.
//		orderRepo.save(order);            // 리포지토리의 save 메서드를 통해 폼에서 제출된 order 객체를 저장 
//		sessionStatus.setComplete();      // 이전 주문 및 연관 타코가 세션에 남아있을 수 있기 때문에 , 세션을 재설정
//		
//		return "redirect:/";
//	}
//	 
//	@GetMapping
//	public String ordersForUser(@AuthenticationPrincipal User user, Model model) {
//		
//		//페이지 크기가 20인 (최근 주문 중 20개까지) 첫번째 페이지 (0)를 요청하기 위해 Pageable 객체를 생성
//		Pageable pageable = PageRequest.of(0, props.getPageSize());  
//		model.addAttribute("orders", orderRepo.findByUserOrderByPlacedAtDesc(user,pageable));
//		return "orderList";
//	}
}
