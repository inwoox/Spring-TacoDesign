package tacos.ingredientclient.feign;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Profile("feign")
@Slf4j

// 의존성을 추가해도 자동구성으로 활성화되지 않으므로, 애노테이션을 추가한다.
@EnableFeignClients
public class FeignClientConfig {
  
  @Bean
  public CommandLineRunner startup() {
    return args -> {
      log.info("**************************************");
      log.info("        Configuring with Feign");
      log.info("**************************************");
    };
  }
  
}
