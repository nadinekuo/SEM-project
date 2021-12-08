package reservationPackage.config;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reservationPackage.entities.Reservation;
import reservationPackage.entities.ReservationType;
import reservationPackage.repositories.ReservationRepository;

@Configuration
public class ReservationConfig {

    @Bean
    CommandLineRunner reservationCommandLineRunner(ReservationRepository reservationRepository) {

        return args -> {
            Reservation reservation1 = new Reservation(ReservationType.EQUIPMENT, 1L, 1L,
                LocalDateTime.of(2020,1,1,1,1));
            Reservation reservation2 = new Reservation(ReservationType.EQUIPMENT, 3L, 2L,
                LocalDateTime.of(2020,1,2,1,1));
            Reservation reservation3 = new Reservation(ReservationType.EQUIPMENT, 5L, 3L,
                LocalDateTime.of(2020,1,3,1,1));
            Reservation reservation4 = new Reservation(ReservationType.EQUIPMENT, 2L, 4L,
                LocalDateTime.of(2020,1,1,1,1));
            Reservation reservation5 = new Reservation(ReservationType.EQUIPMENT, 4L, 5L,
                LocalDateTime.of(2020,1,2,1,1));
            Reservation reservation6 = new Reservation(ReservationType.EQUIPMENT, 5L, 6L,
                LocalDateTime.of(2020,1,3,1,1));

            reservationRepository.saveAll(List.of(reservation1, reservation2));

        };
    }

}
