package sportFacilitiesPackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/{sportRoomId}")
    @ResponseBody
    public SportRoom getSportRoom(@PathVariable Long sportRoomId) {
        try {
            return sportRoomService.getSportRoom(sportRoomId);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/{sportRoomId}/exists")
    @ResponseBody
    public Boolean sportRoomExists(@PathVariable Long sportRoomId) {
        return sportRoomService.sportRoomExists(sportRoomId);
    }

    @GetMapping("/{sportRoomName}/getMaximumCapacity")
    @ResponseBody
    public int getSportRoomMaximumCapacity(@PathVariable Long sportRoomId) {
        try {
            return sportRoomService.getSportRoom(sportRoomId).getMaxCapacity();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/{sportRoomName}/getMinimumCapacity")
    @ResponseBody
    public int getSportRoomMinimumCapacity(@PathVariable Long sportRoomId) {
        try {
            return sportRoomService.getSportRoom(sportRoomId).getMinCapacity();
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
