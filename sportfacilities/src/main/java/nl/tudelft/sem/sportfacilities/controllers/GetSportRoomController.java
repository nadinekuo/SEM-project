package nl.tudelft.sem.sportfacilities.controllers;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import nl.tudelft.sem.sportfacilities.entities.Sport;
import nl.tudelft.sem.sportfacilities.entities.SportRoom;
import nl.tudelft.sem.sportfacilities.services.SportRoomService;

@RestController
@RequestMapping("getSportRoomServices")
public class GetSportRoomController {


    private final transient SportRoomService sportRoomService;

    public GetSportRoomController(SportRoomService sportRoomService) {
        this.sportRoomService = sportRoomService;
    }

    /**
     * Gets sport room.
     *
     * @param sportRoomId the sport room id
     * @return the sport room
     */
    @GetMapping("/{sportRoomId}")
    @ResponseBody
    public ResponseEntity<?> getSportRoom(@PathVariable Long sportRoomId) {
        try {
            SportRoom sportRoom = sportRoomService.getSportRoom(sportRoomId);
            return new ResponseEntity<>(sportRoom, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets sport room name.
     *
     * @param sportRoomId the sport room id
     * @return the sport room name
     */
    @GetMapping("/{sportRoomId}/getName")
    @ResponseBody
    public ResponseEntity<String> getSportRoomName(@PathVariable Long sportRoomId) {
        try {
            SportRoom sportRoom = sportRoomService.getSportRoom(sportRoomId);
            return new ResponseEntity<String>(sportRoom.getSportRoomName(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(maxCapacity.toString(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(minCapacity.toString(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
            Sport relatedSport = sportRoomService.getSportRoom(sportFieldId).getSports().get(0);
            return new ResponseEntity<>(relatedSport.getSportName(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
