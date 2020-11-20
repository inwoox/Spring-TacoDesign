package tacos.web.api;

import java.util.List;

import org.springframework.hateoas.CollectionModel;


public class TacoModels extends CollectionModel<TacoModel> {
  public TacoModels(List<TacoModel> tacoModels) {
	  super(tacoModels);
  }
}