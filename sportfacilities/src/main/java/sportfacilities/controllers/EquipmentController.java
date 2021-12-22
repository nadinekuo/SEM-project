package sportfacilities.controllers;

import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.Equipment;
import sportfacilities.entities.Sport;
import sportfacilities.services.EquipmentService;
import sportfacilities.services.SportService;

/**
 * The type Equipment controller.
 */
@RestController
@RequestMapping("equipment")
public class EquipmentController {

    private final transient EquipmentService equipmentService;
    private final transient SportService sportService;

    @Autowired
    private final transient RestTemplate restTemplate;

    /**
     * Autowired constructor for the class.
     *
     * @param equipmentService equipmentService
     * @param sportService     sportService
     */
    @Autowired
    public EquipmentController(EquipmentService equipmentService, SportService sportService) {
        this.equipmentService = equipmentService;
        this.restTemplate = equipmentService.restTemplate();
        this.sportService = sportService;
    }

    /**
     * Gets equipment.
     *
     * @param equipmentId the equipment id
     * @return the equipment
     */
    @GetMapping("/{equipmentId}")
    @ResponseBody
    public ResponseEntity<?> getEquipment(@PathVariable Long equipmentId) {
        try {
            Equipment equipment = equipmentService.getEquipment(equipmentId);
            return new ResponseEntity<>(equipment, HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets equipment name.
     *
     * @param equipmentId the equipment id
     * @return the equipment name
     */
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

    /**
     * Gets one instance of equipment that is available.
     *
     * @param equipmentName the equipment name, example: "hockeyStick"
     * @return the first available equipment Id, will be -1 if non-existent or not available
     */
    @GetMapping("/{equipmentName}/getAvailableEquipment")
    @ResponseBody
    public ResponseEntity<String> getAvailableEquipment(@PathVariable String equipmentName) {
        Long equipmentId = equipmentService.getAvailableEquipmentIdsByName(equipmentName);
        return new ResponseEntity<String>(equipmentId.toString(), HttpStatus.OK);
    }

    /**
     * Add new equipment.
     *
     * @param equipmentName    the equipment name
     * @param relatedSportName the related sport
     */
    @PutMapping("/{equipmentName}/{relatedSportName}/addNewEquipment/admin")
    @ResponseBody
    public void addNewEquipment(@PathVariable String equipmentName,
                                @PathVariable String relatedSportName) {

        Sport sport = sportService.getSportById(relatedSportName);
        equipmentService.addEquipment(new Equipment(equipmentName, sport, false));
    }

    /**
     * Delete an instance of equipment.
     *
     * @param equipmentId the equipment id
     * @return the response
     */
    @DeleteMapping("/{equipmentId}/deleteEquipment/admin")
    public ResponseEntity<?> deleteEquipment(@PathVariable long equipmentId) {
        try {
            equipmentService.deleteEquipment(equipmentId);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Sets inUse for equipment to false.
     *
     * @param equipmentId the equipment id
     */
    @PostMapping("/{equipmentId}/broughtBack/admin")
    @ResponseBody
    public void equipmentBroughtBack(@PathVariable Long equipmentId) {
        equipmentService.setEquipmentToNotInUse(equipmentId);
    }

    /**
     * Sets inUse for equipment to true.
     *
     * @param equipmentId the equipment id
     */
    @PostMapping("/{equipmentId}/reserved")
    @ResponseBody
    public void equipmentReserved(@PathVariable Long equipmentId) {
        equipmentService.setEquipmentToInUse(equipmentId);
    }

}
