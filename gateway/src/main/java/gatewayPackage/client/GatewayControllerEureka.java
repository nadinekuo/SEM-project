package gatewayPackage.client;

import org.springframework.web.bind.annotation.RequestMapping;

public interface GatewayControllerEureka {


    @RequestMapping("/greeting")
    String greeting();

}
