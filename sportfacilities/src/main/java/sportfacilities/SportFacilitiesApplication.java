package sportfacilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SportFacilitiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportFacilitiesApplication.class, args);
    }

}