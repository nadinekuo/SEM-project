package nl.tudelft.sem.sportfacilities.controllers;

import java.util.NoSuchElementException;
import nl.tudelft.sem.sportfacilities.services.SportRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("setSportRoomServices")
public class SetSportRoomController {

    private final transient SportRoomService sportRoomService;

    public SetSportRoomController(SportRoomService sportRoomService) {
        this.sportRoomService = sportRoomService;
    }

    /**
     * Sets sport room name.
     *
     * @param sportRoomId   the sport room id
     * @param sportRoomName the sport room name
     * @return the sport room name
     */
    @PostMapping("/{sportRoomId}/{sportRoomName}/setSportRoomName/admin")
    @ResponseBody
    public ResponseEntity<?> setSportRoomName(@PathVariable Long sportRoomId,
                                              @PathVariable String sportRoomName) {
        try {
            sportRoomService.setSportRoomName(sportRoomId, sportRoomName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Sets sport room minimum capacity.
     *
     * @param sportRoomId the sport room id
     * @param minCapacity the min capacity
     * @return the sport room minimum capacity
     */
    @PostMapping("/{sportRoomId}/{minCapacity}/setMinimumCapacity/admin")
    @ResponseBody
    public ResponseEntity<?> setSportRoomMinimumCapacity(@PathVariable Long sportRoomId,
                                                         @PathVariable int minCapacity) {
        try {
            sportRoomService.setSportRoomMinCapacity(sportRoomId, minCapacity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Sets sport room maximum capacity.
     *
     * @param sportRoomId the sport room id
     * @param maxCapacity the max capacity
     * @return the sport room maximum capacity
     */
    @PostMapping("/{sportRoomId}/{maxCapacity}/setMaximumCapacity/admin")
    @ResponseBody
    public ResponseEntity<?> setSportRoomMaximumCapacity(@PathVariable Long sportRoomId,
                                                         @PathVariable int maxCapacity) {
        try {
            sportRoomService.setSportRoomMaxCapacity(sportRoomId, maxCapacity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}

