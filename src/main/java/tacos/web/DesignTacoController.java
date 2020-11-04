package tacos.web;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tacos.Taco;
import tacos.data.TacoRepository;


@RestController                        // Controller처럼 뷰의 이름을 반환하지 않고, HTTP 응답 몸체에 쓰는 값을 반환한다.
@RequestMapping(path="/design", produces="application/json")
@CrossOrigin(origins="*")              // 서로 다른 도메인 (호스트,포트 둘 다 또는 둘 중 하나가 다름) 간 요청 허용   /  다른 도메인의 클라이언트에서 이 API를 사용할 수 있게 한다.
public class DesignTacoController {		 // 이 애노테이션으로 CORS (Cross-Origin Resource Sharing) 헤더를 포함할 수 있다 

	private TacoRepository tacoRepo;
	
	@Autowired
	EntityLinks entityLinks;         

	public DesignTacoController(TacoRepository tacoRepo) {
	  this.tacoRepo = tacoRepo;
	}

	// 가장 최근에 생성된 12개의 타코 리스트 반환
	@SuppressWarnings("deprecation")
	@GetMapping("/recent")
	public ResponseEntity<CollectionModel<TacoModel>> recentTacos(){
		PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());  // 오름차순 - 올라간다 - 오래된 것부터 / 내림차순 - 내려간다 - 최신 것부터
		List<Taco> tacos = tacoRepo.findAll(page).getContent();
		
		List<TacoModel> tacoModels = new TacoModelAssembler().toModels(tacos);
		
		// resources 객체를 반환하기 전에 링크를 추가하여, 반환되는 리소스에 링크가 포함되도록 한다.
		// 이렇게하면 이 경로에 대한 GET 요청은 각각 self 링크를 가지는 타코들과, 이 타코들이 포함된 리스트 자체의 recents 링크를 가지는 타코 리스트를 생성한다.
		CollectionModel<TacoModel> recentModels = new CollectionModel<TacoModel>(tacoModels);
		recentModels.add(linkTo(methodOn(DesignTacoController.class).recentTacos()).withRel("recents"));
		return new ResponseEntity<>(recentModels, HttpStatus.OK);							
		
		// recentResources.add("http://localhost:8080/design/recent", "recents");  // 하드코딩
	}
	
	// 요청한 URL (타코 ID) 에 맞는 타코 검색
	@GetMapping("/{id}")            // design/{id} 경로의 get 요청을 처리한다.
	public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) {
		Optional<Taco> optTaco = tacoRepo.findById(id);
		if (optTaco.isPresent()) {    // 해당 ID와 일치하는 타코가 있는지 확인 후, Optional<Taco> 객체의 get()을 호출해 Taco 객체를 반환한다.
			return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(optTaco.get(), HttpStatus.NOT_FOUND);
	}
	
	// 요청 몸체의 JSON 데이터를 가지고, TacoRepo에 타코 저장.
	@PostMapping(consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)							// 응답으로 201 CREATED 상태 코드 반환.
	public Taco postTaco(@RequestBody Taco taco) {  // 요청 몸체의 JSON 데이터가 Taco 객체로 변환 되어 taco 매개변수와 바인딩.
		return tacoRepo.save(taco);										
	}
	
//  폼을 스프링에서 구현할 때 
//	@GetMapping
//  public String showDesignForm(Model model, Principal principal) {
//		List<Ingredient> ingredients = new ArrayList<>();
//    ingredientRepo.findAll().forEach(i -> ingredients.add(i));
//    Type[] types = Ingredient.Type.values();
//    for (Type type : types) {
//      model.addAttribute(type.toString().toLowerCase(),
//          filterByType(ingredients, type));
//    }
//    String username = principal.getName();
//    User user = userRepo.findByUsername(username);
//    model.addAttribute("user", user);
//    return "design";
//  }
//
//  private List<Ingredient> filterByType(
//      List<Ingredient> ingredients, Type type) {
//    return ingredients
//              .stream()
//              .filter(x -> x.getType().equals(type))
//              .collect(Collectors.toList());
//  }
//
//  @ModelAttribute(name = "order")
//  public Order order() {
//    return new Order();
//  }
//
//  @ModelAttribute(name = "taco")
//  public Taco taco() {
//    return new Taco();
//  }
//
//  @PostMapping
//  public String processDesign(
//		  @Valid Taco design, 
//		  Errors errors, @ModelAttribute Order order) {
//	  if (errors.hasErrors()) {
//		 return "design";
//	  }
//	  Taco saved = tacoRepo.save(design);
//	  order.addDesign(saved);
//
//	  return "redirect:/orders/current";
//  }

}
