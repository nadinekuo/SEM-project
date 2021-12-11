package sportfacilities.config;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sportfacilities.entities.Equipment;
import sportfacilities.entities.Sport;
import sportfacilities.entities.SportRoom;
import sportfacilities.repositories.EquipmentRepository;
import sportfacilities.repositories.SportRepository;
import sportfacilities.repositories.SportRoomRepository;

@Configuration
public class SportFacilitiesConfig {

    @Bean
    CommandLineRunner userCommandLineRunner(SportRoomRepository sportRoomRepository,
                                            SportRepository sportRepository,
                                            EquipmentRepository equipmentRepository) {

        return args -> {

            String boxString = "boxingGloves";

            Sport soccer = new Sport("soccer", true, 6, 11);
            Sport hockey = new Sport("hockey", true, 7, 14);
            Sport volleyball = new Sport("volleyball", true, 4, 12);
            Sport tennis = new Sport("tennis", true, 4, 13);
            Sport yoga = new Sport("yoga", false, 1, -1);
            Sport zumba = new Sport("zumba", false, 1, -1);
            Sport kickboxing = new Sport("kickbox", false, 1, -1);

            sportRepository.saveAll(
                List.of(soccer, hockey, volleyball, tennis, yoga, zumba, kickboxing));

            SportRoom hallX1 = new SportRoom("X1", List.of(soccer, hockey), 10, 50);
            SportRoom hallX2 =
                new SportRoom("X2", List.of(hockey, volleyball, tennis, zumba), 15, 60);
            SportRoom hallX3 = new SportRoom("X3", List.of(yoga, zumba, kickboxing), 1, 55);
            SportRoom hockeyField = new SportRoom("hockeyfieldA", List.of(hockey), 10, 200);

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
