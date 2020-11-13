package tacos;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// 유레카 서버

@SpringBootApplication
@EnableEurekaServer
public class MicroserviceRegistryApplication {
 	public static void main(String[] args) {
 		SpringApplication.run(MicroserviceRegistryApplication.class, args);
 	}
}