package tacos.ingredientclient.rest;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;


// 이 클라이언트 모듈의 추가된 WebFlux 의존성에 따라, 이 모듈이 내장된 Netty 웹서버를 가지고 실행되며,
// 8080 포트를 리스닝하게 되기 때문에, 이 모듈에 있는 이 컨트롤러가 localhost:8080/ingredients 요청을 받을 수 있게 된다.


@Controller
@RequestMapping("/ingredients")
@Conditional(NotFeignAndNotWebClientCondition.class)
@Slf4j
public class IngredientController {

  private IngredientServiceClient client;

  public IngredientController(IngredientServiceClient client) {
    this.client = client;
  }
  
  // 이 컨트롤러에서 요청을 받으면, 서비스 클라이언트의 메서드를 호출하여, 리본 클라이언트 로드 밸런서를 사용하여, 유레카에서 서비스를 찾아
  // 선택된 서비스 인스턴스의 호스트와 포트 정보를 가지고 API를 호출하여, 받은 결과를 가져와서 model에 담아 뷰에 출력할 준비를 하고, ingredientList 뷰를 반환한다.
  @GetMapping
  public String ingredientList(Model model) {
    log.info("Fetched all ingredients from a RestTemplate-based service.");
    model.addAttribute("ingredients", client.getAllIngredients());
    return "ingredientList";
  }
  
  @GetMapping("/{id}")
  public String ingredientDetailPage(@PathVariable("id") String id, Model model) {
    log.info("Fetched an ingredient from a RestTemplate-based service.");
    model.addAttribute("ingredient", client.getIngredientById(id));
    return "ingredientDetail";
  }
  
}
