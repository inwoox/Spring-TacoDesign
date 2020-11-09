

package tacos.web.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import tacos.Ingredient;

@Configuration
public class SpringDataRestConfiguration extends RepositoryRestConfigurerAdapter {

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config.exposeIdsFor(Ingredient.class);
  }
  
}

//@Configuration
//public class SpringDataRestConfiguration {
//
//  // 커스텀 하이퍼링크를 스프링 데이터 REST 엔드포인트 (/tacoes)에 추가
//  @Bean
//  public RepresentationModelProcessor<PagedModel<EntityModel<Taco>>> tacoProcessor(EntityLinks links) {
//    return new RepresentationModelProcessor<PagedModel<EntityModel<Taco>>>() {
//      @Override
//      public PagedModel<EntityModel<Taco>> process(PagedModel<EntityModel<Taco>> resource) {
//        resource.add(links.linkFor(Taco.class).slash("recent").withRel("recents"));
//        return resource;
//      }
//    };
//  }
//}
