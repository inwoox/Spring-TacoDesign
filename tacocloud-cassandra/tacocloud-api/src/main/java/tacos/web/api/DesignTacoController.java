package tacos.web.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//end::recents[]
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//tag::recents[]
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Taco;
import tacos.data.TacoRepository;


//	2020.11. 현재까지는 관계형 데이터베이스나 JPA는 리액티브 리포지토리가 지원되지 않는다.


//	매 연결마다 하나의 스레드를 사용하는 스프링 MVC 같은 전형적인 서블릿 기반의 웹 프레임워크는 스레드 블로킹과 다중 스레드 (다중 스레드로 다수의 요청 처리)로 수행 된다. 
//	요청이 처리될 때 스레드 풀에서 작업 스레드를 가져와서 해당 요청을 처리, 작업 스레드 종료시까지 요청 스레드는 블로킹 된다.
//	따라서 요청량의 증가에 따른 확장이 사실상 어렵고, 작업 스레드가 풀로 반환되어 또 다른 요청 처리를 준비하는데 많은 시간이 걸린다.

//	이에 반해 비동기 웹 프레임워크는 더 적은 수의 스레드로 높은 확장성을 가질 수 있다. (이벤트 루핑이라는 기법을 적용하여, 한 스레드당 많은 요청을 처리)
//	스레드는 이벤트 루프를 가지며, 이벤트 루프에서는 해당 작업의 콜백을 등록하여, 병행으로 수행되게 하고, 다른 이벤트 처리로 넘어간다. (모든 것이 이벤트로 처리 된다)
//	결과적으로 소수의 스레드로 많은 요청을 처리할 수 있어, 스레드 관리부담이 줄어들고, 확장이 용이하다.

//	리액티브 프로그래밍 모델을 스프링 MVC에 억지로 집어넣는 대신 별도의 리액티브 웹 프레임워크인 WebFlux를 만들었다.
//	스프링 WebFlux는 서블릿 API에 연결되지 않으므로 실행하기 위해 서블릿 컨테이너를 필요로 하지 않는다.
//	대신에 블로킹이 없는 어떤 웹 컨테이너에서도 실행될 수 있으며, 이에는 Netty, Undertow, Tomcat, Jetty 또는 다른 서블릿 3.1 이상의 컨테이너가 포함된다.

//	MVC와 WebFlux는 같은 애노테이션을 공유한다. 또한 WebFlux를 사용할 때는 기본적인 내장 서버가 톰캣 대신 Netty가 된다.
//	Netty는 몇 안되는 비동기적인 이벤트 중심의 서버 중 하나이며, 리액티브 웹 프레임워크에 잘 맞는다.
//	스프링 WebFlux는 요청이 이벤트 루프로 처리되는 리액티브 웹 프레임워크 / 스프링 MVC는 다중 스레드에 의존하여 다수의 요청을 처리하는 서블릿 기반 웹 프레임워크


@RestController
@RequestMapping(path = "/design", produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoController {
  private TacoRepository tacoRepo;

  public DesignTacoController(TacoRepository tacoRepo) {
    this.tacoRepo = tacoRepo;
  }
  
  //  @GetMapping("/recent")
  //  public Iterable<Taco> recentTacos() {                
  //    PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());  
  //    return tacoRepo.findAll(page).getContent();
  //  }
  
  // 위의 코드를 리액티브 타입인 Flux<Taco>를 반환하도록 WebFlux 컨트롤러로 변경한다.
  // 반환 타입 외에는 MVC 컨트롤러와 다른 점이 없다.
  // 이때 리포지토리는 ReactiveCrudRepository를 상속하는 리액티브형 리포지토리로 변경해야하며, 
  // 리포지토리로부터 Flux<Taco>와 같은 리액티브 타입을 받을 때는 subscribe()를 호출할 필요가 없다. 프레임워크가 호출해준다.
  @GetMapping("/recent")
  public Flux<Taco> recentTacos() {
    return tacoRepo.findAll().take(12);
  }

  // 요청 페이로드가 완전하게 분석 되어 Taco 객체를 생성하는데 사용될 수 있어야 postTaco가 호출될 수 있다.
  // save 메서드의 블로킹 되는 호출이 끝나고 복귀되어야 postTaco()가 끝나고 복귀할 수 있다. 즉, 요청은 두번 블로킹 된다.
  // 그러나 리액티브 코드를 적용하면 완전하게 블로킹 되지 않는 요청 처리 메서드로 만들 수 있다.
  
  //  @PostMapping(consumes = "application/json")
  //  @ResponseStatus(HttpStatus.CREATED)
  //  public Mono<Taco> postTaco(@RequestBody Taco taco) {
  //    return tacoRepo.save(taco);
  //  }
  
  // saveAll 메서드는 Mono, Flux를 포함해 리액티브 스트림의 Publisher 인터페이스를 구현한 어떤 타입도 인자로 받을 수 있다.
  // saveAll 메서드는 Flux<Taco>를 반환하지만, Mono를 인자로 받았으므로, saveAll이 반환하는 Flux는 하나의 Taco 객체만 포함할 것이다.
  // 따라서 next()를 호출하여 Mono<Taco>로 받을 수 있고, 이것을 postTaco가 반환한다.
  // saveAll 메서드는 Mono를 인자로 받으므로 요청 몸체로부터 Taco 객체가 분석되는 것을 기다리지 않고, 즉시 호출되고,
  // 리포지토리 또한 리액티브이므로, saveAll 메서드가 종료될 때까지 기다리지 않고, Mono를 받아 즉시 Flux<Taco>를 반환하므로 블로킹 되지 않는다.
  @PostMapping(consumes = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Taco> postTaco(@RequestBody Mono<Taco> tacoMono) {
    return tacoRepo.saveAll(tacoMono).next();
  }

  // Flux는 0,1 또는 다수의 데이터를 갖는 파이프라인, Mono는 하나의 데이터만 갖는 데이터셋에 최적화된 리액티브 타입
  @GetMapping("/{id}")
  public Mono<Taco> tacoById(@PathVariable("id") UUID id) {
    return tacoRepo.findById(id);
  }
  
//	Rest API를 리액티브하게 사용할 수 있다. 
//	스프링 5는 RestTemplate의 리액티브 대안으로 WebClient를 제공한다.
//	WebClient를 통해 외부 API로 요청할 때 리액티브 타입의 전송과 수신 모두를 할 수 있다.
  
//  Mono<Ingredient> ingredient = WebClient.create()
//		  .get()
//		  .uri("http://localhost:8080/ingredients/{id}", ingredientId)
//		  .retrieve()
//		  .bodyToMono(Ingredient.class);
//  ingredient.subscribe(i -> { ... })

  
//	리액티브와 리액티브가 아닌 타입 간의 변환
//	리액티브 프로그래밍은 클라이언트부터 데이터베이스까지 리액티브 모델을 가질 때 완전하게 발휘되지만,
//	데이터베이스가 리액티브가 아닌 경우에도 일부 장점은 살릴 수 있다
  
//	선택한 데이터베이스가 블로킹 없는 리액티브 쿼리를 지원하지 않더라도,
//	블로킹 되는 방식으로 데이터를 가져온 후, 가능한 빨리 리액티브 타입으로 변환하여 상위 컴포넌트들이 리액티브의 장점을 활용하게 한다.
  
//	예를 들어, List<Order> findByUser(User user)와 같은 메서드가 있을 경우
//	이를 아래와 같이 바꾼다.
  
//  List<Order> orders = repo.findByUser(someUser);
//  Flux<Order> orderFlux = Flux.fromIterable(orders);
//  
//  Order order repo.findById(Long id);
//  Mono<Order> orderMono = Mono.just(order);
  
//	처음에 가져올 때는 블로킹 되는 방식으로 가져오지만 그 이후부터는, 리액티브 타입으로 처리하게 한다.
  
  
//	Order order = orderMono.block();						// Mono에서 객체만 추출 , 추출 작업시 블로킹
//	orderRepo.save(order);
  
//	Iterable<Taco> tacos = tacoFlux.toIterable();			// Flux에서 객체만 추출 , 추출 작업시 블로킹
//	tacoRepo.saveAll(tacos);

//	tacoFlux.subscribe(taco -> { tacoRepo.sava(taco); });	// save는 여전히 블로킹 오퍼레이션이지만, 리액티브 방식의 subscribe()를 사용하므로, 블로킹 방식의 일괄 처리보다는 좋은 방법이다.


}
