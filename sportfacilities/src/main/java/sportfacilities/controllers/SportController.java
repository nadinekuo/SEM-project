package sportfacilities.controllers;

import java.util.NoSuchElementException;
import javax.ws.rs.PUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sportfacilities.entities.Sport;
import sportfacilities.services.SportService;

/**
 * The type Sport controller.
 */
@RestController
@RequestMapping("sport")
public class SportController {

    private final transient SportService sportService;

    /**
     * Instantiates a new Sport controller.
     *
     * @param sportService the sport service
     */
    @Autowired
    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    /**
     * Gets sport max team size.
     *
     * @param sportName the sport name
     * @return the sport max team size
     */
    @GetMapping("/{sportName}/getMaxTeamSize")
    @ResponseBody
    public ResponseEntity<String> getSportMaxTeamSize(@PathVariable String sportName) {
        try {
            Integer maxSize = sportService.getSportById(sportName).getMaxTeamSize();
            return new ResponseEntity<String>(maxSize.toString(), HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("Sport with id " + sportName + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets sport min team size.
     *
     * @param sportName the sport name
     * @return the sport min team size
     */
    @GetMapping("/{sportName}/getMinTeamSize")
    @ResponseBody
    public ResponseEntity<String> getSportMinTeamSize(@PathVariable String sportName) {
        try {
            Integer minSize = sportService.getSportById(sportName).getMinTeamSize();
            return new ResponseEntity<String>(minSize.toString(), HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("Sport with id " + sportName + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Add sport response entity.
     *
     * @param sportName   the sport name
     * @param minCapacity the min capacity
     * @param maxCapacity the max capacity
     * @return the response entity
     */
    @PutMapping("/{sportName}/{minCapacity}/{maxCapacity}/addTeamSport")
    @ResponseBody
    public ResponseEntity<?> addSport(
        @PathVariable String sportName,
        @PathVariable int minCapacity, @PathVariable int maxCapacity){

        sportService.addSport(new Sport(sportName, minCapacity, maxCapacity));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Add sport response entity.
     *
     * @param sportName the sport name
     * @return the response entity
     */
    @PutMapping("/{sportName}/addNonTeamSport")
    @ResponseBody
    public ResponseEntity<?> addSport(@PathVariable String sportName){

        sportService.addSport(new Sport(sportName));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete sport response entity.
     *
     * @param sportName the sport name
     * @return the response entity
     */
    @DeleteMapping("/{sportName}/deleteSport/admin")
    @ResponseBody
    public ResponseEntity<String> deleteSport(@PathVariable String sportName) {
        try{
            sportService.deleteSport(sportName);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("No such element", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
