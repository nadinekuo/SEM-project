package sportFacilitiesPackage.controllers;

import javax.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sportFacilitiesPackage.entities.Sport;
import sportFacilitiesPackage.entities.SportRoom;
import sportFacilitiesPackage.services.SportRoomService;

@RestController
@RequestMapping("sportRoom")
public class SportRoomController {

    private final transient SportRoomService sportRoomService;

    /**
     * Autowired constructor for the class.
     *
     * @param sportRoomService sportRoomService
     */
    @Autowired
    public SportRoomController(SportRoomService sportRoomService) {
        this.sportRoomService = sportRoomService;
    }

    // Get sport room

    @GetMapping("/{sportRoomName}")
    @ResponseBody
    public SportRoom getSportRoom(@PathVariable String sportRoomName) {
        try {
            return sportRoomService.getSportRoomByName(sportRoomName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/{sportRoomName}/getMaximumCapacity")
    @ResponseBody
    public int getSportRoomMaximumCapacity(@PathVariable String sportRoomName) {
        try {
            return sportRoomService.getSportRoomByName(sportRoomName).getMaxCapacity();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/{sportRoomName}/getMinimumCapacity")
    @ResponseBody
    public int getSportRoomMinimumCapacity(@PathVariable String sportRoomName) {
        try {
            return sportRoomService.getSportRoomByName(sportRoomName).getMinCapacity();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        }
    }

/*    @GetMapping("/{sportRoomName}/getSports")
    @ResponseBody
    public List<Sport> getSports(@PathVariable String sportRoomName) {

    }*/


}
