package sportFacilitiesPackage.controllers;

import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sportFacilitiesPackage.entities.Equipment;
import sportFacilitiesPackage.entities.Sport;
import sportFacilitiesPackage.services.EquipmentService;
import sportFacilitiesPackage.services.SportService;

@RestController
@RequestMapping("equipment")
public class EquipmentController {

    private final transient EquipmentService equipmentService;
    private final transient SportService sportService;

    @Autowired
    private final RestTemplate restTemplate;


    /**
     * Autowired constructor for the class.
     *
     * @param equipmentService equipmentService
     * @param sportService
     */
    @Autowired
    public EquipmentController(EquipmentService equipmentService, SportService sportService) {
        this.equipmentService = equipmentService;
        this.restTemplate = equipmentService.restTemplate();
        this.sportService = sportService;
    }

    @GetMapping("/{equipmentId}")
    @ResponseBody
    public Equipment getEquipment(@PathVariable Long equipmentId) {
        try {
            return equipmentService.getEquipment(equipmentId);
        } catch (NoSuchFieldException | NoSuchElementException e) {
            return null;
        }
    }

    @GetMapping("/{equipmentName}/getAvailableEquipment")
    @ResponseBody
    public ResponseEntity<?> getAvailableEquipment(@PathVariable String equipmentName) {
        try {
            Long equipmentId = equipmentService.getAvailableEquipmentIdsByName(equipmentName);
            equipmentService.setEquipmentToInUse(equipmentId);
            ResponseEntity<String> response = new ResponseEntity<String>(equipmentId.toString(),
                HttpStatus.OK);
            return response;
        } catch (NoSuchElementException | NoSuchFieldException e) {
            return new ResponseEntity<>("The equipment requested is not in stock or the "
                + "equipment name was not found",
                HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{equipmentName}/{relatedSportName}/addNewEquipment/admin")
    @ResponseBody
    public void addNewEquipment(@PathVariable String equipmentName,
                                @PathVariable String relatedSportName) throws NoSuchFieldException {

        Sport sport = sportService.getSportById(relatedSportName);
        equipmentService.addEquipment(new Equipment(equipmentName, sport, false));
    }

    @PostMapping("/{equipmentId}/broughtBack/admin")
    @ResponseBody
    public void equipmentBroughtBack(@PathVariable Long equipmentId) {
        equipmentService.setEquipmentToNotInUse(equipmentId);
    }

    @PostMapping("/{equipmentId}/reserved")
    @ResponseBody
    public void equipmentReserved(@PathVariable Long equipmentId) {
        equipmentService.setEquipmentToInUse(equipmentId);
    }


}
