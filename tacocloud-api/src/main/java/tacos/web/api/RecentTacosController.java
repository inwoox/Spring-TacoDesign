package tacos.web.api;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import tacos.Taco;
import tacos.data.TacoRepository;

@RepositoryRestController  // 이 애노테이션이 있는 REST CONTROLLER는 , spring.data.rest.base-path 구성 속성의 값(/api)이 앞에 붙은 경로를 가진다. 
public class RecentTacosController {
	private TacoRepository tacoRepo;
	public RecentTacosController(TacoRepository tacoRepo) {
		this.tacoRepo = tacoRepo;
	}
	
	@GetMapping(path="/tacos/recent", produces="application/hal+json")  // 이 메서드는 /api/tacos/recent의 GET 요청을 처리한다.
	public ResponseEntity<CollectionModel<TacoModel>> recentTacos(){
		PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
		List<Taco> tacos = tacoRepo.findAll(page).getContent();
		List<TacoModel> tacoModels = new TacoModelAssembler().toModels(tacos);
		CollectionModel<TacoModel> recentModels = new CollectionModel<TacoModel>(tacoModels);
		recentModels.add(linkTo(methodOn(RecentTacosController.class).recentTacos()).withRel("recents"));
		return new ResponseEntity<>(recentModels, HttpStatus.OK);
	}
}
