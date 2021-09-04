package ch.mywebsite.yannhoh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MyWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWebsiteApplication.class, args);
    }

}
