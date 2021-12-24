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

            Reservation reservation1 = new Reservation(ReservationType.EQUIPMENT, "hockey", 1L, 2L,
                LocalDateTime.of(2020, 1, 1, 16, 30));

            Reservation reservation2 = new Reservation(ReservationType.EQUIPMENT, "hockey", 2L, 2L,
                LocalDateTime.of(2020, 1, 1, 19, 00));

            Reservation reservation3 =
                new Reservation(ReservationType.SPORTS_ROOM, "Hall 1", 2L, 42L,
                    LocalDateTime.of(2022, 12, 05, 20, 30));

            Reservation reservation4 =
                new Reservation(ReservationType.SPORTS_ROOM, "Hall 2", 2L, 42L,
                    LocalDateTime.of(2022, 12, 05, 22, 30));

            Reservation groupReservation1 =
                new Reservation(ReservationType.SPORTS_ROOM, "Hall 3", 3L, 2L,
                    LocalDateTime.of(2022, 10, 25, 19, 00), 1L);

            Reservation groupReservation2 =
                new Reservation(ReservationType.SPORTS_ROOM, "Hall 4", 4L, 2L,
                    LocalDateTime.of(2022, 10, 25, 19, 00), 1L);

            Reservation groupReservation3 =
                new Reservation(ReservationType.SPORTS_ROOM, "Hall 4", 4L, 2L,
                    LocalDateTime.of(2022, 10, 25, 19, 00), 1L);

            Reservation groupReservation4 =
                new Reservation(ReservationType.SPORTS_ROOM, "Hall 7", 5L, 2L,
                    LocalDateTime.of(2022, 10, 25, 19, 00), 1L);

            reservationRepository.saveAll(
                List.of(reservation1, reservation2, reservation3, reservation4, groupReservation1,
                    groupReservation2, groupReservation3, groupReservation4));

        };
    }

}
