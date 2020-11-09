package tacos.restclient;

import java.net.URI;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Taco;


@Slf4j
@ComponentScan
@SpringBootConfiguration     // 이 애노테이션과 main 함수를 통해, 이 모듈은 빌드 후 독립적으로 실행할 수 있다. 
public class RestExamples {

  public static void main(String[] args) {
	  SpringApplication.run(RestExamples.class, args);
  }

  @Bean
  public RestTemplate restTemplate() {
	  return new RestTemplate();
  }

  // 스프링 데이터 HATEOAS에 같이 제공되며, 스프링 애플리케이션에서 하이퍼미디어 API를 사용할 수 있는 솔루션
  // Traverson에는 이 URL만 지정하면 되며, 이후부터는 각 링크의 관계 이름으로 API를 사용한다.
  @Bean
  public Traverson traverson() {
	  Traverson traverson = new Traverson(URI.create("http://localhost:8080/api/"), MediaTypes.HAL_JSON);
	  return traverson;
  }
	
  
  
  // RestTemplate으로 실행되는 예제 코드
	
  @Bean
  public CommandLineRunner fetchIngredients(TacoCloudClient tacoCloudClient) {
    return args -> {
      log.info("----------------------- GET -------------------------");
      log.info("GETTING INGREDIENT BY IDE");
      log.info("Ingredient:  " + tacoCloudClient.getIngredientById("CHED"));
      log.info("GETTING ALL INGREDIENTS");
      List<Ingredient> ingredients = tacoCloudClient.getAllIngredients();
      log.info("All ingredients:");
      for (Ingredient ingredient : ingredients) {
        log.info("   - " + ingredient);
      }
    };
  }
  
  @Bean
  public CommandLineRunner putAnIngredient(TacoCloudClient tacoCloudClient) {
    return args -> {
      log.info("----------------------- PUT -------------------------");
      Ingredient before = tacoCloudClient.getIngredientById("LETC");
      log.info("BEFORE:  " + before);
      tacoCloudClient.updateIngredient(new Ingredient("LETC", "Shredded Lettuce", Ingredient.Type.VEGGIES));
      Ingredient after = tacoCloudClient.getIngredientById("LETC");
      log.info("AFTER:  " + after);
    };
  }
  
  @Bean
  public CommandLineRunner addAnIngredient(TacoCloudClient tacoCloudClient) {
    return args -> {
      log.info("----------------------- POST -------------------------");
      Ingredient chix = new Ingredient("CHIX", "Shredded Chicken", Ingredient.Type.PROTEIN);
      Ingredient chixAfter = tacoCloudClient.createIngredient(chix);
      log.info("AFTER=1:  " + chixAfter);   
    };
  }

  
  @Bean
  public CommandLineRunner deleteAnIngredient(TacoCloudClient tacoCloudClient) {
    return args -> {
      log.info("----------------------- DELETE -------------------------");
      // start by adding a few ingredients so that we can delete them later...
      Ingredient beefFajita = new Ingredient("BFFJ", "Beef Fajita", Ingredient.Type.PROTEIN);
      tacoCloudClient.createIngredient(beefFajita);
      Ingredient shrimp = new Ingredient("SHMP", "Shrimp", Ingredient.Type.PROTEIN);
      tacoCloudClient.createIngredient(shrimp);

      Ingredient before = tacoCloudClient.getIngredientById("CHIX");
      log.info("BEFORE:  " + before);
      tacoCloudClient.deleteIngredient(before);
      Ingredient after = tacoCloudClient.getIngredientById("CHIX");
      log.info("AFTER:  " + after);
      before = tacoCloudClient.getIngredientById("BFFJ");
      log.info("BEFORE:  " + before);
      tacoCloudClient.deleteIngredient(before);
      after = tacoCloudClient.getIngredientById("BFFJ");
      log.info("AFTER:  " + after);
      before = tacoCloudClient.getIngredientById("SHMP");
      log.info("BEFORE:  " + before);
      tacoCloudClient.deleteIngredient(before);
      after = tacoCloudClient.getIngredientById("SHMP");
      log.info("AFTER:  " + after);
    };
  }
  
  
  
  
  // Traverson으로 실행되는 예제 코드
  
  // Traverson을 통한 모든 식자재 조회
  @Bean
  public CommandLineRunner traversonGetIngredients(TacoCloudClient tacoCloudClient) {
    return args -> {
      Iterable<Ingredient> ingredients = tacoCloudClient.getAllIngredientsWithTraverson();
      log.info("----------------------- GET INGREDIENTS WITH TRAVERSON -------------------------");
      for (Ingredient ingredient : ingredients) {
        log.info("   -  " + ingredient);
      }
    };
  }
  
  // Traverson과 RestTemplate를 이용해 데이터 추가 , RestTemplate를 이용해 데이터 삭제
  @Bean
  public CommandLineRunner traversonSaveIngredient(TacoCloudClient tacoCloudClient) {
    return args -> {
      Ingredient pico = tacoCloudClient.addIngredient(
          new Ingredient("PICO", "Pico de Gallo", Ingredient.Type.SAUCE));
      List<Ingredient> allIngredients = tacoCloudClient.getAllIngredients();
      log.info("----------------------- ALL INGREDIENTS AFTER SAVING PICO -------------------------");
      for (Ingredient ingredient : allIngredients) {
        log.info("   -  " + ingredient);
      }
      pico = tacoCloudClient.getIngredientById("PICO");
      tacoCloudClient.deleteIngredient(pico);
      log.info("----------------------- PICO DELETED -------------------------");
    };
  }
  
//  @Bean
//  public CommandLineRunner traversonRecentTacos(TacoCloudClient tacoCloudClient) {
//    return args -> {
//      Iterable<Taco> recentTacos = tacoCloudClient.getRecentTacosWithTraverson();
//      log.info("----------------------- GET RECENT TACOS WITH TRAVERSON -------------------------");
//      for (Taco taco : recentTacos) {
//        log.info("   -  " + taco);
//      }
//    };
//  }

}
