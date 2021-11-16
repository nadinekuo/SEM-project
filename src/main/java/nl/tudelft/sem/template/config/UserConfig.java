package nl.tudelft.sem.template.config;

import java.util.List;
import nl.tudelft.sem.template.entities.User;
import nl.tudelft.sem.template.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner userCommandLineRunner(UserRepository userRepository) {

        return args -> {
            User arslan = new User(1L, "arslan123", "password1", true);
            User emil = new User(2L, "emil123", "password2", false);
            User emma = new User(3L, "emma123", "password3", true);
            User erwin = new User(4L, "erwin123", "password4", false);
            User nadine = new User(5L, "nadine123", "password5", true);
            User panagiotis = new User(6L, "panas123", "password6", false);

            userRepository.saveAll(List.of(arslan, emil, emma, erwin, nadine, panagiotis));

        };
    }

}
