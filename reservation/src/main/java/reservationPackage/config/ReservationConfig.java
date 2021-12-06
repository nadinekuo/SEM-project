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
            Reservation reservation1 =
                new Reservation(ReservationType.EQUIPMENT, 1L, 2L,
                    LocalDateTime.of(2020,1,1,1,1), false);

            Reservation reservation2 =
                new Reservation(ReservationType.EQUIPMENT ,2L, 2L,
                    LocalDateTime.of(2020,1,1,1,1), false);

            reservationRepository.saveAll(List.of(reservation1, reservation2));

        };
    }

}
