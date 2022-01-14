package nl.tudelft.sem.sportfacilities.config;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import nl.tudelft.sem.sportfacilities.entities.Equipment;
import nl.tudelft.sem.sportfacilities.entities.Lesson;
import nl.tudelft.sem.sportfacilities.entities.Sport;
import nl.tudelft.sem.sportfacilities.entities.SportRoom;
import nl.tudelft.sem.sportfacilities.repositories.EquipmentRepository;
import nl.tudelft.sem.sportfacilities.repositories.LessonRepository;
import nl.tudelft.sem.sportfacilities.repositories.SportRepository;
import nl.tudelft.sem.sportfacilities.repositories.SportRoomRepository;

@Configuration
public class SportFacilitiesConfig {

    @Bean
    CommandLineRunner userCommandLineRunner(SportRoomRepository sportRoomRepository,
                                            SportRepository sportRepository,
                                            EquipmentRepository equipmentRepository,
                                            LessonRepository lessonRepository) {

        return args -> {

            LocalDateTime startingDate = LocalDateTime.of(2021, 1, 1, 1, 1);
            LocalDateTime endingDate = LocalDateTime.of(2021, 1, 1, 1, 1);
            Lesson lesson1 = new Lesson("Tango", startingDate, endingDate, 10);
            Lesson lesson2 = new Lesson("HIT", startingDate, endingDate, 10);
            Lesson lesson3 = new Lesson("PoleDancing", startingDate, endingDate, 10);
            Lesson lesson4 = new Lesson("Spinning", startingDate, endingDate, 10);

            lessonRepository.saveAll(List.of(lesson1, lesson2, lesson3, lesson4));


            String boxString = "boxingGloves";

            Sport soccer = new Sport("soccer",  6, 11);
            Sport hockey = new Sport("hockey",  7, 14);
            Sport volleyball = new Sport("volleyball",  4, 12);
            Sport tennis = new Sport("tennis", 4, 13);
            Sport yoga = new Sport("yoga");
            Sport zumba = new Sport("zumba");
            Sport kickboxing = new Sport("kickbox");

            sportRepository.saveAll(
                List.of(soccer, hockey, volleyball, tennis, yoga, zumba, kickboxing));

            SportRoom hallX1 = new SportRoom("X1", List.of(soccer, hockey), 10, 50, true);
            SportRoom hallX2 =
                new SportRoom("X2", List.of(hockey, volleyball, tennis, zumba), 15, 60, true);
            SportRoom hallX3 = new SportRoom("X3", List.of(yoga, zumba, kickboxing), 1, 55, true);
            SportRoom hockeyField = new SportRoom("hockeyfieldA", List.of(hockey), 10, 200, false);

            sportRoomRepository.saveAll(List.of(hallX1, hallX2, hallX3, hockeyField));

            Equipment hockeyStick = new Equipment("hockeyStick", hockey, true);
            Equipment boxingGloves1 = new Equipment(boxString, kickboxing, false);
            Equipment boxingGloves2 = new Equipment(boxString, kickboxing, true);
            Equipment boxingGloves3 = new Equipment(boxString, kickboxing, false);
            Equipment boxingGloves4 = new Equipment(boxString, kickboxing, true);
            Equipment soccerBall1 = new Equipment("soccerBall", soccer, false);
            Equipment soccerBall2 = new Equipment("soccerBall", soccer, true);
            Equipment soccerBall3 = new Equipment("soccerBall", soccer, false);

            equipmentRepository.saveAll(
                List.of(hockeyStick, boxingGloves1, boxingGloves2, boxingGloves3, boxingGloves4,
                    soccerBall1, soccerBall2, soccerBall3));
        };
    }

}
