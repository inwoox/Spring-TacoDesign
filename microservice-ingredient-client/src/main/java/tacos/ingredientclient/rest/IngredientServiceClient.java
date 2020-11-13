package tacos.ingredientclient.rest;

import java.util.Arrays;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tacos.ingredientclient.Ingredient;

@Service
@Conditional(NotFeignAndNotWebClientCondition.class)  // 이 클래스에 정의된 조건이 참인 경우에만 빈을 생성한다.
public class IngredientServiceClient {

  private RestTemplate rest;
  
  // 로드밸런싱된 RestTemplate을 필요로 하는 빈에 주입한다.
  public IngredientServiceClient(@LoadBalanced RestTemplate rest) {
    this.rest = rest;
  }
  
  // 호스트와 포트 대신 서비스 이름을 사용하여 API를 호출한다.
  // 내부적으로는 해당 서비스 이름을 찾아, 인스턴스를 선택하도록 RestTemplate이 리본에 요청한다.
  // 그리고 선택된 서비스 인스턴스의 호스트와 포트 정보를 포함하도록, 리본이 URL을 변경한 후 원래대로 RestTemplate이 사용된다.
  public Ingredient getIngredientById(String ingredientId) {
    return rest.getForObject("http://ingredient-service/ingredients/{id}", Ingredient.class, ingredientId);
  }
  
  public Iterable<Ingredient> getAllIngredients() {
    Ingredient[] ingredients = rest.getForObject("http://ingredient-service/ingredients", Ingredient[].class);
    return Arrays.asList(ingredients);
  }
  
}
