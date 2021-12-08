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

            Equipment hockeyStick = new Equipment(1L, "hockeyStick", hockey, true);
            Equipment boxingGloves1 = new Equipment(2L, "boxingGloves",  kickboxing, false);
            Equipment boxingGloves2 = new Equipment(6L, "boxingGloves",  kickboxing, true);
            Equipment boxingGloves3 = new Equipment(4L, "boxingGloves",  kickboxing, false);
            Equipment boxingGloves4 = new Equipment(10L, "boxingGloves",  kickboxing, true);
            Equipment soccerBall1 = new Equipment(3L, "soccerBall", soccer, false);
            Equipment soccerBall2 = new Equipment(5L, "soccerBall", soccer, true);
            Equipment soccerBall3 = new Equipment(11L, "soccerBall", soccer, false);

            equipmentRepository.saveAll(List.of(hockeyStick, boxingGloves1, boxingGloves2,
                boxingGloves3, boxingGloves4,  soccerBall1, soccerBall2, soccerBall3));
        };
    }


}
