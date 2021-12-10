package sportfacilities.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
    public int getSportMaxTeamSize(@PathVariable String sportName) {
        return sportService.getSportById(sportName).getMaxTeamSize();
    }

    /**
     * @param sportName the sport id
     * @return the min team size for this sport
     */
    @GetMapping("/{sportName}/getMinTeamSize")
    @ResponseBody
    public int getSportMinTeamSize(@PathVariable String sportName) {
        return sportService.getSportById(sportName).getMinTeamSize();
    }


}
