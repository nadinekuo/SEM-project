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
import sportfacilities.services.SportRoomService;

/**
 * The type Sport room controller.
 */
@RestController
@RequestMapping("sportRoom")
public class SportRoomController {

    private final transient SportRoomService sportRoomService;

    /**
     * Instantiates a new Sport room controller.
     *
     * @param sportRoomService the sport room service
     */
    @Autowired
    public SportRoomController(SportRoomService sportRoomService) {
        this.sportRoomService = sportRoomService;
    }


    /**
     * Response entity containing boolean, whether or not sport room exists in database.
     *
     * @param sportRoomId the sport room id
     * @return the response entity
     */
    @GetMapping("/{sportRoomId}/exists")
    @ResponseBody
    public ResponseEntity<String> sportRoomExists(@PathVariable Long sportRoomId) {
        Boolean exists = sportRoomService.sportRoomExists(sportRoomId);
        return new ResponseEntity<>(exists.toString(), HttpStatus.OK);
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
            return new ResponseEntity<>(isHall.toString(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Add sport room response entity.
     *
     * @param name        the name
     * @param sport       the sport
     * @param minCapacity the min capacity
     * @param maxCapacity the max capacity
     * @param isSportHall the is sport hall
     * @return the response entity
     */
    @PutMapping("/{name}/{sport}/{minCapacity}/{maxCapacity}/{isSportHall}/addSportRoom/admin")
    @ResponseBody
    public ResponseEntity<?> addSportRoom(@PathVariable String name, @PathVariable String sport,
                                          @PathVariable int minCapacity,
                                          @PathVariable int maxCapacity,
                                          @PathVariable boolean isSportHall) {

        sportRoomService.addSportRoom(name, sport, minCapacity, maxCapacity, isSportHall);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * Delete sport room response entity.
     *
     * @param sportRoomId the sport room id
     * @return the response entity
     */
    @DeleteMapping("/{sportRoomId}/deleteSportRoom/admin")
    public ResponseEntity<String> deleteSportRoom(@PathVariable Long sportRoomId) {
        try {
            sportRoomService.deleteSportRoom(sportRoomId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Add sport to sports hall response entity.
     *
     * @param sportRoomId the sport room id
     * @param sportName   the sport name
     * @return the response entity
     */
    @PostMapping("/{sportRoomId}/{sportName}/addSportToSportHall/admin")
    @ResponseBody
    public ResponseEntity<?> addSportToSportsHall(@PathVariable Long sportRoomId,
                                                  @PathVariable String sportName) {
        try {
            sportRoomService.addSportToSportsHall(sportRoomId, sportName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}