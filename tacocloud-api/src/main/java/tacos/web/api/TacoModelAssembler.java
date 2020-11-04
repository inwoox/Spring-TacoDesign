package tacos.web.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import tacos.Taco;

public class TacoModelAssembler extends RepresentationModelAssemblerSupport<Taco, TacoModel> {
	
	// 슈퍼클래스의 생성자를 호출하며, 이때 TacoModel을 생성하면서, 만들어지는 링크에 포함되는 기본 경로를 결정하기 위해 , DesignTacoController를 사용한다.
	public TacoModelAssembler() {
		super(DesignTacoController.class, TacoModel.class);  
	}																										
	
	// 타코 객체로 TacoModel 인스턴스만 생성
	@Override
	protected TacoModel instantiateModel(Taco taco) {
		return new TacoModel(taco);
	}
	
	// 타코 객체로 TacoModel 인스턴스를 생성 / 타코 객체의 id 값으로 생성되는 self 링크가 URL에 지정됨.
	@Override
	public TacoModel toModel(Taco taco) {
		return createModelWithId(taco.getId(), taco);
	}
	
	public List<TacoModel> toModels(List<Taco> tacos) {
		List<TacoModel> models = new ArrayList<TacoModel>();
		for (Taco taco : tacos) {
			models.add(new TacoModel(taco));
		}
		
		return models;
	}
}
