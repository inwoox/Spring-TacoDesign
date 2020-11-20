package tacos.ingredientclient.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

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
  @HystrixCommand(fallbackMethod="getDefaultIngredientDetails")
  public Ingredient getIngredientById(String ingredientId) {
    return rest.getForObject("http://ingredient-service/ingredients/{id}", Ingredient.class, ingredientId);
  }
  //public Ingredient getIngredientById(String ingredientId) {
  //  return rest.getForObject("http://ingredient-service/ingredients/{id}", Ingredient.class, ingredientId);
  //}
  
  private Ingredient getDefaultIngredientDetails(String ingredientId) {
	  if (ingredientId.equals("FLTO")) {
	      return new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
	  } 
	  else if (ingredientId.equals("GRBF")) {
	      return new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);
	  } 
	  else {
	      return new Ingredient("CHED", "Shredded Cheddar", Ingredient.Type.CHEESE);
	  }
  }
  
  
  // @HystrixCommand - 서킷 브레이커를 적용하고 있다. exchange에서 예외를 처리하지 않으므로, 호출자 메서드에서 예외를 처리해야하고, 거기서도 처리하지 않으면,
  // 호출 스택의 그다음 상위 호출자로 예외가 전달되고, 어떤 호출자도 예외를 처리하지 않는다면, 결구 최상위 호출자 (마이크로서비스나 클라이언트)에서 에러로 처리된다.
  // 이처럼 처리가 되지 않은 unchecked 예외는 어떤 애플리케이션에서도 골칫거리이며, 특히 마이크로서비스의 경우가 그렇다.
  // 장애가 생기면 베가스 규칙을 적용해야한다. (마이크로서비스에서 생긴 에러는, 해당 마이크로서비스에서 처리하는 것) / 서킷 브레이커를 선언하면 이런 규칙을 충족시킨다.
  // 서킷 브레이커를 선언할 때는 @HystrixCommand를 메서드에 지정하고, 실패시 동작할 폴백 메서드를 지정하면 된다.
  
  // 폴백 메서드는 원래 메서드와 시그니처(매개변수, 리턴 타입)가 동일해야한다.
  // 또한 폴백 체인을 만들 수 있지만, 폴백 스택의 제일 밑에는 실행에 실패하지 않는, 서킷 브레이커가 필요없는 메서드가 있어야 한다.
  // 기본적으로 @HystrixCommand가 지정된 모든 메서드는 1초 후에 타임아웃 되고, 이 메서드의 폴백 메서드가 호출된다.
  
  // 고려 시간(20초) 동안에, 호출 임계 횟수와 실패 호출 비율이 모두 초과될 경우 , 서킷은 열림 상태가 되며, 이후의 모든 호출은 폴백 메서드가 처리한다.
  // 열림 유지 시간 (60초) 이후, 절반-열림 상태가 되며, 원래 메서드에 대한 호출이 가능하다. 
  @HystrixCommand(fallbackMethod="getDefaultIngredients",	
	      commandProperties={																			// Histrix 명령 속성 배열
	          @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="500"),   // 타임아웃 0.5초
	          @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="30"),				// 호출 임계 횟수 
	          @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="25"),				// 실패 호출 비율
              @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="20000"),			// 요청 횟수와 실패 비율 고려 시간
              @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="60000")			// 열림 상태의 서킷 유지 시간
	      })
  public Iterable<Ingredient> getAllIngredients() {
	  ParameterizedTypeReference<List<Ingredient>> stringList = new ParameterizedTypeReference<List<Ingredient>>() {};
	  return rest.exchange("http://ingredient-service/ingredients", HttpMethod.GET,HttpEntity.EMPTY, stringList).getBody();
  }
//  public Iterable<Ingredient> getAllIngredients() {
//    Ingredient[] ingredients = rest.getForObject("http://ingredient-service/ingredients", Ingredient[].class);
//    return Arrays.asList(ingredients);
//  }
  
  private Iterable<Ingredient> getDefaultIngredients() {
	  List<Ingredient> ingredients = new ArrayList<>();
	  ingredients.add(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
	  ingredients.add(new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN));
	  ingredients.add(new Ingredient("CHED", "Shredded Cheddar", Ingredient.Type.CHEESE));
	  return ingredients;
  }
}
