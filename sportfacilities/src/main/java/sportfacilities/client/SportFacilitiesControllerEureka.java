package sportfacilities.client;

import org.springframework.web.bind.annotation.RequestMapping;

public interface SportFacilitiesControllerEureka {
    @RequestMapping("/greeting")
    String greeting();
}
