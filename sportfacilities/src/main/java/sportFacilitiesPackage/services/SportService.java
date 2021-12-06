package sportFacilitiesPackage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportFacilitiesPackage.entities.Sport;
import sportFacilitiesPackage.repositories.SportRepository;

@Service
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
