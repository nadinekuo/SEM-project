package sportfacilities.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.SportRoom;
import sportfacilities.services.SportRoomService;

/**
 * The type Sport room controller.
 */
@RestController
@RequestMapping("sportRoom")
public class SportRoomController {

    @Autowired
    private final transient RestTemplate restTemplate;

    private final transient SportRoomService sportRoomService;

    /**
     * Autowired constructor for the class.
     *
     * @param sportRoomService sportRoomService
     */
    @Autowired
    public SportRoomController(SportRoomService sportRoomService) {
        this.sportRoomService = sportRoomService;
        this.restTemplate = sportRoomService.restTemplate();
    }

    // Get sport room

    /**
     * Gets sport room.
     *
     * @param sportRoomId the sport room id
     * @return the sport room
     */
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

    /**
     * Indicates if a sportRoom with that id exists.
     *
     * @param sportRoomId the sport room id
     * @return true if it exists
     */
    @GetMapping("/{sportRoomId}/exists")
    @ResponseBody
    public Boolean sportRoomExists(@PathVariable Long sportRoomId) {
        return sportRoomService.sportRoomExists(sportRoomId);
    }

    /**
     * Gets sport room maximum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room maximum capacity
     */
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

    /**
     * Gets sport room minimum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room minimum capacity
     */
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
