package tacos.web;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import tacos.Ingredient;
import tacos.Ingredient.Type;

public class IngredientModel extends RepresentationModel<IngredientModel>{  // ResourceSupport 클래스의 최선 버전

		@Getter
		private String name;
		
		@Getter
		private Type type;
		
		public IngredientModel(Ingredient ingredient) {
			this.name = ingredient.getName();
			this.type = ingredient.getType();
		}
}
