

package tacos.restclient;

import java.util.Collection;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Taco;

@Service
@Slf4j
public class TacoCloudClient {
	
	// Traverson은 API를 이동하면서 리소스를 가져오기 쉽지만, 쓰기나 삭제를 제공하지 않음
	// RestTemplate은 쓰거나 삭제할 수 있지만, 이동하기가 쉽지 않다
	private RestTemplate rest;
	private Traverson traverson;
	
	public TacoCloudClient(RestTemplate rest, Traverson traverson) {
		this.rest = rest;
		this.traverson = traverson;
	}
	
	
	// RestTemplate 사용 
	
	// REST API를 호출하여 식자재 가져오기
	public Ingredient getIngredientById(String ingredientId) {  
		return rest.getForObject("http://localhost:8080/ingredients/{id}", Ingredient.class, ingredientId); // URL, 응답이 바인딩 되는 타입, 플레이스홀더인 {id}에 들어가는 매개변수.
	}
	
	// getForObject와 같은 방법으로 동작하나, 도메인 객체를 반환하는 대신, 도메인 객체를 포함하는 ResponseEntity 객체를 반환한다.
	public Ingredient getIngredientById2(String ingredientId) {
		ResponseEntity<Ingredient> responseEntity = rest.getForEntity("http://localhost:8080/api/ingredients/{id}", Ingredient.class, ingredientId);
		log.info("Fetched time: " + responseEntity.getHeaders().getDate());  // 예를 들어 이런 형태와 같이, 응답 날짜 정보를 알고 싶을때, getForEntity를 사용한다.
		return responseEntity.getBody();
	}
	
	// 모든 식자재 반환  /   exchange : 지정한 URL에 지정한 메서드 요청을 하고, ResponseEntity를 반환)
	public List<Ingredient> getAllIngredients() {
	    return rest.exchange("http://localhost:8080/ingredients",
	    		HttpMethod.GET, null, new ParameterizedTypeReference<List<Ingredient>>() {}).getBody();
	  }
	
	// 식자재 추가
	public Ingredient createIngredient(Ingredient ingredient) {
		return rest.postForObject("http://localhost:8080/ingredients", ingredient, Ingredient.class);
	}
	
	// Ingredient를 전송하여 식자재 교체
	public void updateIngredient(Ingredient ingredient) {
		rest.put("http://localhost:8080/ingredients/{id}", ingredient, ingredient.getId());
	}
	
	// 식자재 삭제
	public void deleteIngredient(Ingredient ingredient) {
		rest.delete("http://localhost:8080/ingredients/{id}", ingredient.getId());
	}
	
	
	
	// Traverson 사용 
	
	// Traverson 빈을 생성할 때 기본 URL을 포함하고, 이후부터는 각 링크의 관계 이름으로 API를 사용한다.
	public Iterable<Ingredient> getAllIngredientsWithTraverson() {
    ParameterizedTypeReference<CollectionModel<Ingredient>> ingredientType = new ParameterizedTypeReference<CollectionModel<Ingredient>>() {};
    
    // follow 메서드를 통해 리소스 링크의 관계 이름이 ingredients인 리소스로 이동 / toObject(읽어들일 객체 타입)를 호출하여 해당 리소스의 콘텐츠를 가져온다 
    // 자바에서는 런타임 시에 제네릭 타입의 타입 정보가 소거되기 때문에, ParameterizedTypeReference를 사용하여 리소스 타입을 지정한다.
    
    CollectionModel<Ingredient> ingredientRes = traverson.follow("ingredients").toObject(ingredientType);
    Collection<Ingredient> ingredients = ingredientRes.getContent();
    return ingredients;
  }
	
	public Iterable<Taco> getRecentTacosWithTraverson() {
    ParameterizedTypeReference<CollectionModel<Taco>> tacoType = new ParameterizedTypeReference<CollectionModel<Taco>>() {};
    CollectionModel<Taco> tacoRes = traverson.follow("tacoes").follow("recents").toObject(tacoType);  // .follow("tacoes","recents")처럼 가능
    return tacoRes.getContent();
  }
	
	
	
	
	// RestTemplate , Traverson 같이 사용
	public Ingredient addIngredient(Ingredient ingredient) {
    String ingredientsUrl = traverson.follow("ingredients").asLink().getHref();  // ingredients 링크를 따라가서, asLink를 통해, ingredients의 링크 자체를 요청, getHref를 사용해 링크의 URL 획득
    return rest.postForObject(ingredientsUrl, ingredient, Ingredient.class);     // 획득한 ingredients 경로를 가지고, 식자재 추가
  }
	
}



// 리소스를 생성하고, 리소스의 위치를 반환
// public URI createIngredient(Ingredient ingredient) {
//   return rest.postForLocation("http://localhost:8080/ingredients", ingredient, Ingredient.class);
// }

// 리소스를 생성하고, 리소스 객체를 포함하는 ResponseEntity를 반환 (Entity에서 리소스 위치를 얻을 수 있다)
// public Ingredient createIngredient(Ingredient ingredient) {
//   ResponseEntity<Ingredient> responseEntity = rest.postForEntity("http://localhost:8080/ingredients",ingredient,Ingredient.class);
//   log.info("New resource created at " + responseEntity.getHeaders().getLocation());
//   return responseEntity.getBody();
// }

// 리소스를 생성하고, 리소스 객체를 반환
// public Ingredient getIngredientById(String ingredientId) {
//   Map<String, String> urlVariables = new HashMap<>();
//   urlVariables.put("id", ingredientId);
//   return rest.getForObject("http://localhost:8080/ingredients/{id}", Ingredient.class, urlVariables);
// }

// public Ingredient getIngredientById(String ingredientId) {
//   Map<String, String> urlVariables = new HashMap<>();
//   urlVariables.put("id", ingredientId);
//   URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/ingredients/{id}").build(urlVariables);
//   return rest.getForObject(url, Ingredient.class);
// }
