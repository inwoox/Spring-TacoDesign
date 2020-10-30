package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String>{  // 첫번째 매개변수는 리포에 저장되는 개체 타입, 두번째는 개체 ID 타입
																																									 // IngredientRepository는 매개변수 타입이 Ingredient, String이어야 한다.
}
