package tacos.web.api;

import java.util.Date;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import tacos.Taco;

@Relation(value="taco", collectionRelation="tacos") 							// TacoModel 객체 리스트가 CollectionModel 객체에서 사용될 때 tacos라는 이름이 되도록 지정 
public class TacoModel extends RepresentationModel<TacoModel> {   // JSON에서는 TacoModel 객체가 taco로 참조 된다.

	private static final IngredientModelAssembler ingredientAssembler = new IngredientModelAssembler();
	
	@Getter
	private final String name;
	
	@Getter
	private final Date createdAt;
	
	@Getter
	private final List<IngredientModel> ingredients;
	
	public TacoModel(Taco taco) {
		this.name = taco.getName();
		this.createdAt = taco.getCreatedAt();
		this.ingredients = ingredientAssembler.toModels(taco.getIngredients()); 
		//this.ingredients = taco.getIngredients();
	}
}
