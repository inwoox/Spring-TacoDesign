
package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// spring-cloud-starter-config 의존성을 추가하고, 이 클라이언트 애플리케이션을 실행하면, 
// 자동 구성이 실행되어 , 구성 서버로부터 속성들을 가져온다. (구성 서버는 Git 리포지토리에서 속성을 가져오는 것이다)
// 구성 서버의 위치는 application.yml에 설정한다.


@SpringBootApplication
public class ClientApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
}
