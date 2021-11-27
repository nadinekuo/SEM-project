package sportFacilitiesPackage.config;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sportFacilitiesPackage.entities.Equipment;
import sportFacilitiesPackage.entities.Sport;
import sportFacilitiesPackage.entities.SportRoom;
import sportFacilitiesPackage.repositories.EquipmentRepository;
import sportFacilitiesPackage.repositories.SportRepository;
import sportFacilitiesPackage.repositories.SportRoomRepository;

@Configuration
public class SportFacilitiesConfig {


    @Bean
    CommandLineRunner userCommandLineRunner(SportRoomRepository sportRoomRepository,
                                            SportRepository sportRepository,
                                            EquipmentRepository equipmentRepository) {

        return args -> {

            Sport soccer = new Sport("soccer", true, 6, 11);
            Sport hockey = new Sport("hockey", true, 7, 14);
            Sport volleyball = new Sport("volleyball", true, 4, 12);
            Sport tennis = new Sport("tennis", true, 4, 13);
            Sport yoga = new Sport("yoga", false, 1, -1);
            Sport zumba = new Sport("zumba", false, 1, -1);
            Sport kickboxing = new Sport("kickbox", false, 1, -1);

            sportRepository.saveAll(List.of(soccer, hockey, volleyball, tennis, yoga, zumba, kickboxing));

            SportRoom hallX1 = new SportRoom("X1", List.of(soccer, hockey), 10, 50);
            SportRoom hallX2 = new SportRoom("X2", List.of(hockey, volleyball, tennis, zumba), 15,
                60);
            SportRoom hallX3 = new SportRoom("X3", List.of(yoga, zumba, kickboxing), 12, 55);

            sportRoomRepository.saveAll(List.of(hallX1, hallX2, hallX3));

            Equipment hockeystick = new Equipment("hockeystick", 60, 12, hockey);
            Equipment kickboxGloves = new Equipment("kickboxGloves", 35, 6, kickboxing);
            Equipment soccerBall = new Equipment("soccerBallWhite", 40, 23, soccer);

            equipmentRepository.saveAll(List.of(hockeystick, kickboxGloves, soccerBall));
        };
    }


}
