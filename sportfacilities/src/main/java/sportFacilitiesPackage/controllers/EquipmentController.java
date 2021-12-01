package sportFacilitiesPackage.controllers;

import java.util.List;
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
    private final RestTemplate restTemplate;


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

    @GetMapping("/{equipmentName}/getAvailableEquipment")
    @ResponseBody
    public Long getAvailableEquipment(@PathVariable String equipmentName) {
        try {
            Long equipmentId = equipmentService.getAvailableEquipmentIdsByName(equipmentName);
            equipmentService.setEquipmentToInUse(equipmentId);
            return equipmentId;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }


    @PostMapping("/{equipmentId}/broughtBack")
    @ResponseBody
    public void equipmentBroughtBack(@PathVariable Long equipmentId) {
        equipmentService.setEquipmentToNotInUse(equipmentId);
    }

    @PostMapping("/{equipmentId}/reserved")
    @ResponseBody
    public void equipmentReserved(@PathVariable Long equipmentId) {
        equipmentService.setEquipmentToInUse(equipmentId);
    }


    @GetMapping("/hello")
    public Long getId() {
        String methodSpecificUrl = "/hey";
        return restTemplate.getForObject(EurekaHelperFields.reservationUrl + methodSpecificUrl, Long.class);
    }

}
