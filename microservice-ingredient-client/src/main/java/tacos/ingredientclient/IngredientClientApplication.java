package tacos.ingredientclient;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHystrix			// 히스트릭스 활성화
public class IngredientClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(IngredientClientApplication.class, args);
	}
}
