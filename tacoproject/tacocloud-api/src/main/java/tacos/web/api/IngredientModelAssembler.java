package tacos.web.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import tacos.Ingredient;

public class IngredientModelAssembler extends RepresentationModelAssemblerSupport<Ingredient, IngredientModel>{
	
	public IngredientModelAssembler() {
		super(IngredientController.class, IngredientModel.class);
	}
	
	@Override
	public IngredientModel toModel(Ingredient ingredient) {
		return createModelWithId(ingredient.getId(), ingredient);
	}
	
	@Override
	protected IngredientModel instantiateModel(Ingredient ingredient) {
		return new IngredientModel(ingredient);
	}
	
	public List<IngredientModel> toModels(List<Ingredient> ingredients) {
		List<IngredientModel> models = new ArrayList<IngredientModel>();
		for (Ingredient ingredient : ingredients) {
			models.add(new IngredientModel(ingredient));
		}
		
		return models;
	}
}
