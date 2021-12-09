package security.client;

import org.springframework.web.bind.annotation.RequestMapping;

public interface SecurityControllerEureka {
    @RequestMapping("/greeting")
    String greeting();
}
