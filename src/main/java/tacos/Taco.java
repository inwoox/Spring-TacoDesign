package tacos;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Taco {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private Date createdAt;
	
	@NotNull
	@Size(min=5, message="Name must be at least 5 characters long")
	private String name;
	
	@ManyToMany(targetEntity=Ingredient.class)  // 하나의 Ingredient는 여러 Taco 객체에 포함될 수 있다. 하나의 Taco에는 여러 Ingredient가 포함될 수 있다.
	@Size(min=1, message="Your must choose at least 1 ingredient")
	private List<Ingredient> ingredients;
	
	@PrePersist         // (PrePersist - Taco 객체가 저장되기 전에) createdAt 속성을 현재 일자와 시간으로 설정하는데에 사용된다.
	void createdAt() {
		this.createdAt = new Date();
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Ingredient> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	
}
