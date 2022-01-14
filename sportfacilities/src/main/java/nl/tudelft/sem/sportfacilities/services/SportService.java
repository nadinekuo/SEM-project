package nl.tudelft.sem.sportfacilities.services;

import java.util.NoSuchElementException;
import nl.tudelft.sem.sportfacilities.entities.Sport;
import nl.tudelft.sem.sportfacilities.repositories.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            () -> new NoSuchElementException("Sport with id " + sportName + " does not exist!"));
    }

    /**
     * Delete sport.
     *
     * @param sportName the sport name
     * @throws IllegalStateException the no such element exception
     */
    public void deleteSport(String sportName) throws IllegalStateException {
        Sport sport = getSportById(sportName);
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
