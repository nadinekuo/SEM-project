package lessonPackage.config;

import lessonPackage.entities.Lesson;
import lessonPackage.repositories.LessonRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LessonConfig {


    @Bean
    CommandLineRunner userCommandLineRunner(LessonRepository lessonRepository) {

        return args -> {
            Lesson tennisLesson1 = new Lesson(1L, "Tennis Monday 11-12 15PM");

            lessonRepository.saveAll(List.of(tennisLesson1));
        };
    }


}
