package securityPackage;

import com.netflix.discovery.EurekaClient;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import securityPackage.client.SecurityControllerEureka;

@SpringBootApplication
@RestController
public class SecurityApplication implements SecurityControllerEureka {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Override
    public String greeting() {
        return String.format(
            "Hello from '%s'!", eurekaClient.getApplication(appName).getName());
    }
}