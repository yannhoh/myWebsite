package ch.mywebsite.yannhoh.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            User richi = new User("richi", "123", "he@ho.com");
            User richi2 = new User("richi2", "123", "he@ho2.com");

            repository.save(richi);
            repository.save(richi2);
        };
    }
}
