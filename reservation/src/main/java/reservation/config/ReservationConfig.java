package reservation.config;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.repositories.ReservationRepository;

/**
 * The type Reservation config.
 */
@Configuration
public class ReservationConfig {

    /**
     * Reservation command line runner command line runner.
     *
     * @param reservationRepository the reservation repository
     * @return the command line runner
     */
    @Bean
    CommandLineRunner reservationCommandLineRunner(ReservationRepository reservationRepository) {

        return args -> {
            Reservation reservation1 = new Reservation(ReservationType.EQUIPMENT, 1L, 1L,
                LocalDateTime.of(2020, 1, 1, 1, 1), false);
            Reservation reservation2 = new Reservation(ReservationType.EQUIPMENT, 3L, 2L,
                LocalDateTime.of(2020, 1, 1, 1, 1), false);
            Reservation reservation3 = new Reservation(ReservationType.EQUIPMENT, 5L, 3L,
                LocalDateTime.of(2020, 1, 1, 1, 1), false);
            Reservation reservation4 = new Reservation(ReservationType.EQUIPMENT, 2L, 4L,
                LocalDateTime.of(2020, 1, 1, 1, 1), false);
            Reservation reservation5 = new Reservation(ReservationType.EQUIPMENT, 4L, 5L,
                LocalDateTime.of(2020, 1, 1, 1, 1), false);
            Reservation reservation6 = new Reservation(ReservationType.EQUIPMENT, 5L, 6L,
                LocalDateTime.of(2020, 1, 1, 1, 1), false);

            reservationRepository.saveAll(List.of(reservation1, reservation2));

        };
    }

}
