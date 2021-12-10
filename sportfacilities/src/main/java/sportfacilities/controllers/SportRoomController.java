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


    /**
     * Gets sport room.
     *
     * @param sportRoomId the sport room id
     * @return the sport room
     */
    @GetMapping("/{sportRoomId}")
    @ResponseBody
    public SportRoom getSportRoom(@PathVariable Long sportRoomId) {
        return sportRoomService.getSportRoom(sportRoomId);
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
     * @param sportRoomId the sports room id
     * @return if the to be reserved sports room is a hall, meaning it holds multiple sports.,
     */
    @GetMapping("/{sportRoomId}/isHall")
    @ResponseBody
    public Boolean sportRoomIsHall(@PathVariable Long sportRoomId) {
        return sportRoomService.getSportRoom(sportRoomId).getIsSportsHall();
    }


    /**
     * Gets sport room maximum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room maximum capacity
     */
    @GetMapping("/{sportRoomId}/getMaximumCapacity")
    @ResponseBody
    public int getSportRoomMaximumCapacity(@PathVariable Long sportRoomId) {
        return sportRoomService.getSportRoom(sportRoomId).getMaxCapacity();
    }

    /**
     * Gets sport room minimum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room minimum capacity
     */
    @GetMapping("/{sportRoomId}/getMinimumCapacity")
    @ResponseBody
    public int getSportRoomMinimumCapacity(@PathVariable Long sportRoomId) {
        return sportRoomService.getSportRoom(sportRoomId).getMinCapacity();
    }

    /**
     * @param sportFieldId - id of sport field to be reserved
     * @return String - name of related Sport (id of Sport)
     *    example: soccer, hockey, ...
     */
    @GetMapping("/{sportFieldId}/getSport")
    @ResponseBody
    public String getSportFieldSport(@PathVariable Long sportFieldId) {
        return sportRoomService.getSportRoom(sportFieldId).getSports()
            .get(0).toString();
    }


}
