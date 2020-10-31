package tacos.web;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

@Controller
@RequestMapping("/design")
@SessionAttributes("order")        // 다수의 HTTP 요청에 걸쳐 존재하는 객체(order)를 지정한다.
public class DesignTacoController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final IngredientRepository ingredientRepo;
	private TacoRepository tacoRepo;
	private UserRepository userRepo;
	
	@ModelAttribute(name = "order")  // order 객체가 모델에 생성되도록 해준다.
	public Order order() {
		return new Order();
	}
	
	@ModelAttribute(name = "taco")  // taco 객체가 모델에 생성되도록 해준다.
	public Taco taco() {
		return new Taco();
	}
	
	
	@Autowired  // 기존에 만들어 둔 IngredientRepository 인터페이스를 주입 (구현체 클래스까지 만들고나서 , 인터페이스를 주입한다)
	public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository tacoRepo, UserRepository userRepo) {
		this.ingredientRepo = ingredientRepo;
		this.tacoRepo = tacoRepo;
		this.userRepo = userRepo;
	}
	
	@GetMapping
	public String showDesignForm(Model model, Principal principal) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));   // 하드코딩하여 데이터를 넣는대신 DB에서 읽어온다. 
		
		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
		
		String username = principal.getName();
		User user = userRepo.findByUsername(username);  // 인증된 사용자를 가져와서 model에 저장
		model.addAttribute("user", user);
		
		return "design";
	}
	
	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type){
		return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}
	
	@PostMapping // 이 메서드에 붙은 @ModelAttribute : 이 order 매개변수는 모델로부터 전달 / 스프링MVC가 이 매개변수에 요청 매개변수를 바인딩 하지 말아야한다는 것을 나타낸다.
	public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order) { 
		if(errors.hasErrors()) {
			return "design";
		}
		Taco saved = tacoRepo.save(design);
		order.addDesign(saved);
		
		logger.info("processing design : " + design);
		return "redirect:/orders/current";
	}
}
