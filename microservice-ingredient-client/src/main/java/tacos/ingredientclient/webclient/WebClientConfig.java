package tacos.ingredientclient.webclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;



// webclient 프로파일이 적용되어 애플리케이션이 동작하면, 구성 클래스가 로드밸런싱된 WebClient.Builder 빈을 생성하고, 로그 출력.

@Configuration
@Profile("webclient")
@Slf4j
public class WebClientConfig {

  @Bean
  @LoadBalanced
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }
  
  @Bean
  public CommandLineRunner startup() {
    return args -> {
      log.info("**************************************");
      log.info("     Configuring with WebClient");
      log.info("**************************************");
    };
  }
}
