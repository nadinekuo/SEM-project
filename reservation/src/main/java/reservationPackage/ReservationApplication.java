package reservationPackage;
import org.springframework.boot.CommandLineRunner;
import reservationPackage.entities.EquipmentNameStrategy;
import reservationPackage.repositories.ReservationRepository;

import com.netflix.discovery.EurekaClient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reservationPackage.client.ReservationControllerEureka;
import reservationPackage.controllers.ReservationController;
import reservationPackage.entities.BasicPremiumUserStrategy;
import reservationPackage.entities.BookingSystem;
import reservationPackage.entities.ChronologicalStrategy;
import reservationPackage.entities.Reservation;
import reservationPackage.entities.ReservationType;
import reservationPackage.repositories.ReservationRepository;
import reservationPackage.services.ReservationService;

@SpringBootApplication
@RestController
public class ReservationApplication implements ReservationControllerEureka {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {

        BookingSystem bookings =
            new BookingSystem(new EquipmentNameStrategy(ReservationService.restTemplate()));

        Reservation reservation1 = new Reservation(ReservationType.EQUIPMENT, 1L, 1L,
            LocalDateTime.of(2020,1,1,1,1));
        Reservation reservation2 = new Reservation(ReservationType.EQUIPMENT, 3L, 2L,
            LocalDateTime.of(2020,1,2,1,1));
        Reservation reservation3 = new Reservation(ReservationType.EQUIPMENT, 5L, 3L,
            LocalDateTime.of(2020,3,3,1,1));
        Reservation reservation4 = new Reservation(ReservationType.EQUIPMENT, 2L, 4L,
            LocalDateTime.of(2021,1,1,1,1));
        Reservation reservation5 = new Reservation(ReservationType.EQUIPMENT, 4L, 5L,
            LocalDateTime.of(2020,2,2,1,1));
        Reservation reservation6 = new Reservation(ReservationType.EQUIPMENT, 5L, 6L,
            LocalDateTime.of(2020,1,3,1,1));


        bookings.addReservation(reservation1);
        bookings.addReservation(reservation2);
        bookings.addReservation(reservation3);
        bookings.addReservation(reservation4);
        bookings.addReservation(reservation5);
        bookings.addReservation(reservation6);

        Reservation first = bookings.getNextReservation();
        System.out.println(bookings.toString());
        //Reservation second = bookings.getNextReservation();
        //System.out.print(first.toString());


        SpringApplication.run(ReservationApplication.class, args);

    }

    @Override
    public String greeting() {
        return String.format(
            "Hello from '%s'!", eurekaClient.getApplication(appName).getName());
    }
}