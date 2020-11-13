package tacos.ingredientclient.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import tacos.ingredientclient.Ingredient;


// ingredient-service라는 이름으로 유레카에 등록된 서비스를 사용해서 식자재를 가져오는 클라이언트를 작성하고 싶을 경우
// 아래와 같이 인터페이스를 구현하면 된다.
// 런타임 시에 Feign이 이 인터페이스를 찾아, 자동으로 구현 클래스를 생성한 후 , 컨텍스트에 빈으로 노출시킨다.

@FeignClient("ingredient-service")	// 모든 메서드는 이 서비스에 대한 요청을 나타낸다.
public interface IngredientClient {

  @GetMapping("/ingredients/{id}")	// 컨트롤러 대신 클라이언트에 지정되어 있다. / 이 메서드 호출시, 리본이 선택한 호스트+포트의 /ingredients/{id} 경로로 GET 요청
  Ingredient getIngredient(@PathVariable("id") String id);

  @GetMapping("/ingredients")
  Iterable<Ingredient> getAllIngredients();

}
