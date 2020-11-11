package tacos.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


//	이메일 통합 모듈을 사용하기 위해서는 , IMAP 메일 서버를 설치하고, application.yml 파일에 , 그에 맞는 정보를 설정한다.

@SpringBootApplication
public class TacoEmailApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TacoEmailApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}