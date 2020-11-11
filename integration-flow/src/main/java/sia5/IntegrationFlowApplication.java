package sia5;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;



//	IntegrationFlow는 타코 클라우드와 관계 없는 스프링 부트 프로젝트  /  파일 통합 플로우 코드를 가지고 있다.
//	/tmp/sia5/files 경로에 simple.txt라는 파일을 쓰는 파일 통합 플로우

//	빌드를 하고나서, 실행시킬 때 JVM 옵션을 통해, Profile을 설정한다. 
//	java -Dspring.profiles.active=javaconfig -jar target/integration-flow-0.0.8-SNAPSHOT.jar
//	이렇게 하면 javaconfig 프로파일이 적용되어 실행된다.


@SpringBootApplication
public class IntegrationFlowApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(IntegrationFlowApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner writeData(FileWriterGateway gateway, Environment env) {
		return args -> {
			String[] activeProfiles = env.getActiveProfiles();
			if (activeProfiles.length > 0) {
				String profile = activeProfiles[0];
				gateway.writeToFile("simple.txt", "Hello, Spring Integration! (" + profile + ")");
			}
			else {
				System.out.println("No active profile set, Should set active profile to one of xmlconfig, javaconfig, or java dsl.");
			}
		};
	}
}