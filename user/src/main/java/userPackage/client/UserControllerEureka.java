package userPackage.client;

import org.springframework.web.bind.annotation.RequestMapping;

public interface UserControllerEureka {
    @RequestMapping("/greeting")
    String greeting();

    @RequestMapping("/adminGreeting/admin")
    String adminGreeting();

}
