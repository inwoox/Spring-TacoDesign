package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DockerRunApplication {

    @GetMapping("/hello")
    public String Hello() {
        return "hello";
    }

    public static void main(String[] args) {
        SpringApplication.run(DockerRunApplication.class, args);
    }

}