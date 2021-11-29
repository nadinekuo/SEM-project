package sportFacilitiesPackage.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import sportFacilitiesPackage.entities.Sport;
import sportFacilitiesPackage.repositories.SportRepository;

public class SportService {
    private final transient SportRepository sportRepository;

    /**
     * Constructor for UserService.
     *
     * @param sportRepository - retrieves Sports from database.
     */
    @Autowired
    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    public Sport getSportById(String sportName) throws NoSuchFieldException {
        return sportRepository.findById(sportName).orElseThrow();
    }

}
