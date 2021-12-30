package sportfacilities;

import com.netflix.discovery.EurekaClient;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import sportfacilities.client.SportFacilitiesControllerEureka;

@SpringBootApplication
@RestController
public class SportFacilitiesApplication {

    @Autowired
    @Lazy
    private transient EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private transient String appName;

    public static void main(String[] args) {
        SpringApplication.run(SportFacilitiesApplication.class, args);
    }

}