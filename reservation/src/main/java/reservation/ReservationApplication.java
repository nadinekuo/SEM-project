package reservation;

import com.netflix.discovery.EurekaClient;
import java.time.LocalDateTime;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import reservation.client.ReservationControllerEureka;
import reservation.entities.BookingSystem;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.UserIdStrategy;
import reservation.services.ReservationService;

/**
 * The type Reservation application.
 */
@SpringBootApplication
@RestController
public class ReservationApplication implements ReservationControllerEureka {

    @Autowired
    @Lazy
    private transient EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private transient String appName;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        BookingSystem bookings =
            new BookingSystem(new UserIdStrategy());

        Reservation reservation1 =
            new Reservation(ReservationType.EQUIPMENT, 1L, 1L, LocalDateTime.of(2020, 1, 1, 1, 1),
                false);
        Reservation reservation2 =
            new Reservation(ReservationType.EQUIPMENT, 3L, 2L, LocalDateTime.of(2020, 1, 2, 1, 1),
                false);
        Reservation reservation3 =
            new Reservation(ReservationType.EQUIPMENT, 5L, 3L, LocalDateTime.of(2020, 3, 3, 1, 1),
                false);
        Reservation reservation4 =
            new Reservation(ReservationType.EQUIPMENT, 3L, 4L, LocalDateTime.of(2021, 1, 1, 1, 1),
                false);
        Reservation reservation5 =
            new Reservation(ReservationType.EQUIPMENT, 4L, 5L, LocalDateTime.of(2020, 2, 2, 1, 1),
                false);
        Reservation reservation6 =
            new Reservation(ReservationType.EQUIPMENT, 5L, 6L, LocalDateTime.of(2020, 1, 3, 1, 1),
                false);

        bookings.addReservation(reservation1);
        bookings.addReservation(reservation2);
        bookings.addReservation(reservation3);
        bookings.addReservation(reservation4);
        bookings.addReservation(reservation5);
        bookings.addReservation(reservation6);

        System.out.println(bookings);

        SpringApplication.run(ReservationApplication.class, args);

    }

    @Override
    public String greeting() {
        return String.format("Hello from '%s'!", eurekaClient.getApplication(appName).getName());
    }
}