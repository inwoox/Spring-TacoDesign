
package tacos.messaging;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

	
  // 메시지 변환기 변경할 때, 이렇게 하면 스프링 부트 자동 구성에서 이 빈을 찾아서 기본 메시지 변환기 대신
  // 이 빈을 RabbitTemplate으로 주입한다.
	
  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

}
