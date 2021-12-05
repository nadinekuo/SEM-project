package reservationPackage;

import com.netflix.discovery.EurekaClient;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reservationPackage.client.ReservationControllerEureka;
import reservationPackage.entities.BookingSystem;
import reservationPackage.entities.ChronologicalStrategy;
import reservationPackage.entities.Reservation;
import reservationPackage.entities.ReservationType;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class ReservationApplication implements ReservationControllerEureka {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationApplication.class, args);
    }

    @Override
    public String greeting() {
        return String.format(
            "Hello from '%s'!", eurekaClient.getApplication(appName).getName());
    }
}