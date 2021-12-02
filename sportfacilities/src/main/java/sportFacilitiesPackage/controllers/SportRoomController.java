package sportFacilitiesPackage.controllers;

import static sportFacilitiesPackage.controllers.EurekaHelperFields.userUrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sportFacilitiesPackage.entities.SportRoom;
import sportFacilitiesPackage.services.SportRoomService;

@RestController
@RequestMapping("sportRoom")
public class SportRoomController {

    @Autowired
    private final RestTemplate restTemplate;

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
    
    public Boolean getUserIsPremium(@PathVariable Long userId){
        String methodSpecificUrl = "/user/" + userId + "/isPremium";

        Boolean b =
            restTemplate.getForObject(userUrl + methodSpecificUrl, Boolean.class);

        return b;
    }


/*    @GetMapping("/{sportRoomName}/getSports")
    @ResponseBody
    public List<Sport> getSports(@PathVariable String sportRoomName) {

    }*/

}
