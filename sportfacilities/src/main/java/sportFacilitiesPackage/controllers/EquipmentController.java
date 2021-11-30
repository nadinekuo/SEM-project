package sportFacilitiesPackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sportFacilitiesPackage.entities.Equipment;
import sportFacilitiesPackage.entities.SportRoom;
import sportFacilitiesPackage.services.EquipmentService;
import sportFacilitiesPackage.services.SportRoomService;

@RestController
@RequestMapping("equipment")
public class EquipmentController {

    private final transient EquipmentService equipmentService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Autowired constructor for the class.
     *
     * @param equipmentService equipmentService
     */
    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
        this.restTemplate = equipmentService.restTemplate();
    }

    @GetMapping("/{equipmentId}")
    @ResponseBody
    public Equipment getEquipment(@PathVariable Long equipmentId) {
        try {
            return equipmentService.getEquipment(equipmentId);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/hello")
    public Long getId() {
        String methodSpecificUrl = "/hey";
        return restTemplate.getForObject(EurekaHelperFields.reservationUrl + methodSpecificUrl, Long.class);
    }

}
