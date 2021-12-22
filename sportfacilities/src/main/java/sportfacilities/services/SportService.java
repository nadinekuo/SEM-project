package sportfacilities.services;

import java.util.NoSuchElementException;
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
     * Gets sport by id.
     *
     * @param sportName the sport name
     * @return the sport by id
     */
    public Sport getSportById(String sportName) {
        return sportRepository.findById(sportName).orElseThrow(
            () -> new IllegalStateException("Sport with id " + sportName + " does not exist!"));
    }

    /**
     * Delete sport.
     *
     * @param sportName the sport name
     * @throws IllegalStateException the no such element exception
     */
    public void deleteSport(String sportName) throws IllegalStateException {
        boolean exists = sportRepository.existsById(sportName);
        if (!exists) {
            throw new IllegalStateException(
                "Sport with id " + sportName + " does not " + "exist!");
        }
        sportRepository.deleteById(sportName);
    }

    /**
     * Add sport.
     *
     * @param sport the sport
     */
    public void addSport(Sport sport) {
        sportRepository.save(sport);
    }
}
