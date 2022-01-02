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
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.strategy.BookingSystem;
import reservation.entities.strategy.UserIdStrategy;

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
        SpringApplication.run(ReservationApplication.class, args);
    }

    @Override
    public String greeting() {
        return String.format("Hello from '%s'!", eurekaClient.getApplication(appName).getName());
    }
}