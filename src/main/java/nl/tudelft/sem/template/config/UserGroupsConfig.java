package nl.tudelft.sem.template.config;

import java.util.List;
import nl.tudelft.sem.template.entities.Group;
import nl.tudelft.sem.template.entities.User;
import nl.tudelft.sem.template.repositories.GroupRepository;
import nl.tudelft.sem.template.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserGroupsConfig {

    @Bean
    CommandLineRunner userCommandLineRunner(UserRepository userRepository,
                                            GroupRepository groupRepository) {

        return args -> {
            User arslan = new User("arslan123", "password1", true);
            User emil = new User("emil123", "password2", false);
            User emma = new User("emma123", "password3", true);
            User erwin = new User("erwin123", "password4", false);
            User nadine = new User("nadine123", "password5", true);
            User panagiotis = new User("panas123", "password6", false);

            userRepository.saveAll(List.of(arslan, emil, emma, erwin, nadine, panagiotis));

            Group g1 = new Group("soccerTeam1", List.of(arslan, emil));
            Group g2 = new Group("volleyballTeam3", List.of(emma, erwin));

            groupRepository.saveAll(List.of(g1, g2));

        };
    }

}
