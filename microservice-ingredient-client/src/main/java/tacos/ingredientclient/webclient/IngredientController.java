package tacos.ingredientclient.webclient;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;


// 요청을 처리하는 컨트롤러 메서드 안에서, 서비스 클라이언트를 통해 (그 안의 리본 클라이언트 로드밸런서를 통해) 유레카에서 서비스를 찾아, 
// 그 서비스의 호스트와 포트 정보를 가지고와서, API를 호출하여 결과를 가지고와서 model에 담고, 뷰 이름을 리턴한다. 

@Controller
@RequestMapping("/ingredients")
@Profile("webclient")			
@Slf4j
public class IngredientController {

  private IngredientServiceClient client;

  public IngredientController(IngredientServiceClient client) {
    this.client = client;
  }
  
  @GetMapping
  public String ingredientList(Model model) {
    log.info("Fetched all ingredients from a WebClient-based service.");
    model.addAttribute("ingredients", client.getAllIngredients());
    return "ingredientList";
  }
  
  @GetMapping("/{id}")
  public String ingredientDetailPage(@PathVariable("id") String id, Model model) {
    log.info("Fetched an ingredient from a WebClient-based service.");
    model.addAttribute("ingredient", client.getIngredientById(id));
    return "ingredientDetail";
  }
  
}
