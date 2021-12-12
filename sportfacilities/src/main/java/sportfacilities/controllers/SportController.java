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

}
