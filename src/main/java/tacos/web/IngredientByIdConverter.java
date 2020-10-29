package tacos.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import tacos.Ingredient;
import tacos.data.IngredientRepository;

@Component    // 스프링에 의해 자동 생성 및 주입되는 빈으로 생성된다.
public class IngredientByIdConverter implements Converter<String, Ingredient> {
	private IngredientRepository ingredientRepo;
	
	@Autowired  // IngredientRepository 인터페이스를 구현한 빈 (JdbcIngredientRepository) 인스턴스가 생성자의 인자로 주입된다.
	public IngredientByIdConverter(IngredientRepository ingredientRepo) {
		this.ingredientRepo = ingredientRepo;
	}
	
	@Override  // 이런 타입 변환이 필요할 때 convert 메서드가 자동 호출 / String 타입의 식자재 ID를 사용해 , DB에 저장된 식자재 데이터를 읽어 Ingredient 객체로 변환하기 위해 사용된다.
	public Ingredient convert(String id) {
		return ingredientRepo.findById(id);
	}
}
