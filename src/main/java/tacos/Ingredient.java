package tacos;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Ingredient {
	
	@Id
	private String id = "";
	private String name = "";
	private Type type = null;
	
	public Ingredient() { // JPA에서는 개체가 인자 없는 생성자를 가져야한다.
		
	}
	
	public Ingredient(String id, String name, Type type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}
	
	public static enum Type {
		WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
	}
}
