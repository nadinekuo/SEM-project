package gateway;

import com.netflix.discovery.EurekaClient;
import gateway.client.GatewayControllerEureka;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableZuulProxy
public class GatewayApplication implements GatewayControllerEureka {


    @Autowired
    @Lazy
    private transient EurekaClient eurekaClient;


    @Value("${spring.application.name}")
    private transient String appName;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Override
    public String greeting() {
        return String.format(
            "Hello from '%s'!", eurekaClient.getApplication(appName).getName());
    }
}
