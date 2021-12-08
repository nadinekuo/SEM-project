package sportFacilitiesPackage.controllers;

import static sportFacilitiesPackage.controllers.EurekaHelperFields.reservationUrl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/{equipmentId}/getEquipmentName")
    @ResponseBody
    public String getEquipmentName(@PathVariable Long equipmentId) {
        try {
            return equipmentService.getEquipmentName(equipmentId);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/{equipmentName}/getAvailableEquipment")
    @ResponseBody
    public ResponseEntity<String> getAvailableEquipment(@PathVariable String equipmentName) {
        try {
            Long equipmentId = equipmentService.getAvailableEquipmentIdsByName(equipmentName);
            equipmentService.setEquipmentToInUse(equipmentId);
            return new ResponseEntity<String>(equipmentId.toString(), HttpStatus.CREATED);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Specified equipment was not found",
                HttpStatus.BAD_REQUEST);
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
        return restTemplate.getForObject(reservationUrl + methodSpecificUrl, Long.class);
    }

}
