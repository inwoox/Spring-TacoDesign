package tacos.kitchen.messaging.jms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import tacos.Order;


// JMS는 몇가지 단점이 있지만, 그 중에 가장 중요한 것은 JMS가 자바 명세이므로 자바 어플리케이션에서만 사용할 수 있다는 것이다.
// 그래서 여러 플랫폼에서 자유로운 RabbitMQ와 카프카 같은 더 새로운 메시징 시스템을 많이 사용한다.

@Profile({"jms-template", "jms-listener"})
@Configuration
public class MessagingConfig {

  @Bean
  public MappingJackson2MessageConverter messageConverter() {
    MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
    messageConverter.setTypeIdPropertyName("_typeId");
    Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
    typeIdMappings.put("order", Order.class);
    messageConverter.setTypeIdMappings(typeIdMappings);
    
    return messageConverter;
  }

}
