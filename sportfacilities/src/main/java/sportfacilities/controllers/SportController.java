package sportfacilities.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sportfacilities.services.SportService;

@RestController
@RequestMapping("sport")
public class SportController {

    private final transient SportService sportService;

    /**
     * Autowired constructor for the class.
     *
     * @param sportService sportService
     */
    @Autowired
    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    /**
     * @param sportName the sport id
     * @return the max team size for this sport
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
     * @param sportName the sport id
     * @return the min team size for this sport
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

}
