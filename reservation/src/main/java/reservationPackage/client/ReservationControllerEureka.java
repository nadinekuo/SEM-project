package reservationPackage.client;

import org.springframework.web.bind.annotation.RequestMapping;

public interface ReservationControllerEureka {
    @RequestMapping("/greeting")
    String greeting();
}
