package sportfacilities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportfacilities.entities.Sport;
import sportfacilities.repositories.SportRepository;

/**
 * The type Sport service.
 */
@Service
public class SportService {
    private final transient SportRepository sportRepository;

    /**
     * Instantiates a new Sport service.
     *
     * @param sportRepository the sport repository
     */
    @Autowired
    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    /**
     * Get sport by id sport.
     *
     * @param sportName the sport name
     * @return the sport
     */
    public Sport getSportById(String sportName) {
        return sportRepository.findById(sportName).orElseThrow(
            () -> new IllegalStateException("Sport with id " + sportName + " does not exist!"));
    }

}
