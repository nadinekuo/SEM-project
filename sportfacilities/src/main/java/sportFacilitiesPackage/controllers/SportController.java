package sportFacilitiesPackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sportFacilitiesPackage.services.LessonService;
import sportFacilitiesPackage.services.SportService;

@RestController
@RequestMapping("sports")
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
    

}
