package sportfacilities.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.Sport;
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
     * Instantiates a new Sport room controller.
     *
     * @param sportRoomService the sport room service
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
     * Sport room exists response entity.
     *
     * @param sportRoomId the sport room id
     * @return the response entity
     */
    @GetMapping("/{sportRoomId}/exists")
    @ResponseBody
    public ResponseEntity<String> sportRoomExists(@PathVariable Long sportRoomId) {
        try {
            Boolean exists = sportRoomService.sportRoomExists(sportRoomId);
            return new ResponseEntity<String>(exists.toString(), HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("Sport room with id " + sportRoomId + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Sport room is hall response entity.
     *
     * @param sportRoomId the sport room id
     * @return the response entity
     */
    @GetMapping("/{sportRoomId}/isHall")
    @ResponseBody
    public ResponseEntity<String> sportRoomIsHall(@PathVariable Long sportRoomId) {
        try {
            Boolean isHall = sportRoomService.getSportRoom(sportRoomId).getIsSportsHall();
            return new ResponseEntity<String>(isHall.toString(), HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("Sport room with id " + sportRoomId + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets sport room maximum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room maximum capacity
     */
    @GetMapping("/{sportRoomId}/getMaximumCapacity")
    @ResponseBody
    public ResponseEntity<String> getSportRoomMaximumCapacity(@PathVariable Long sportRoomId) {
        try {
            Integer maxCapacity = sportRoomService.getSportRoom(sportRoomId).getMaxCapacity();
            return new ResponseEntity<String>(maxCapacity.toString(), HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("Sport room with id " + sportRoomId + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets sport room minimum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room minimum capacity
     */
    @GetMapping("/{sportRoomId}/getMinimumCapacity")
    @ResponseBody
    public ResponseEntity<String> getSportRoomMinimumCapacity(@PathVariable Long sportRoomId) {
        try {
            Integer minCapacity = sportRoomService.getSportRoom(sportRoomId).getMinCapacity();
            return new ResponseEntity<String>(minCapacity.toString(), HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("Sport room with id " + sportRoomId + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets sport field sport.
     *
     * @param sportFieldId the sport field id
     * @return the sport field sport
     */
    @GetMapping("/{sportFieldId}/getSport")
    @ResponseBody
    public ResponseEntity<String> getSportFieldSport(@PathVariable Long sportFieldId) {
        try {
            Sport relatedSport = sportRoomService.getSportRoom(sportFieldId)
                .getSports().get(0);
            return new ResponseEntity<String>(relatedSport.getSportName(), HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("Sport field with id " + sportFieldId + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}
