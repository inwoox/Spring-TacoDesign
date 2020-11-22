package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BootAdminClientApplication {

    @GetMapping("/hello")
    public String Hello() {
        return "hello";
    }

    public static void main(String[] args) {
        SpringApplication.run(BootAdminClientApplication.class, args);
    }

}