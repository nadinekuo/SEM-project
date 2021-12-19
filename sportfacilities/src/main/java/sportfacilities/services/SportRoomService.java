package sportfacilities.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.Sport;
import sportfacilities.entities.SportRoom;
import sportfacilities.repositories.SportRoomRepository;

/**
 * The type Sport room service.
 */
@Service
public class SportRoomService {
    private final transient SportRoomRepository sportRoomRepository;
    private final transient SportService sportService;

    /**
     * Instantiates a new Sport room service.
     *
     * @param sportRoomRepository the sport room repository
     */
    @Autowired
    public SportRoomService(SportRoomRepository sportRoomRepository, SportService sportService) {
        this.sportRoomRepository = sportRoomRepository;
        this.sportService = sportService;
    }

    /**
     * Gets sport room.
     *
     * @param sportRoomId the sport room id
     * @return the sport room
     */
    public SportRoom getSportRoom(Long sportRoomId) {
        return sportRoomRepository.findBySportRoomId(sportRoomId).orElseThrow(
            () -> new IllegalStateException(
                "Sport room with id " + sportRoomId + " does not exist!"));
    }

    /**
     * Sport room exists boolean.
     *
     * @param sportRoomId the sport room id
     * @return the boolean
     */
    public Boolean sportRoomExists(Long sportRoomId) {
        return sportRoomRepository.findBySportRoomId(sportRoomId).isPresent();
    }

    /**
     * Adds a new sport room.
     *
     * @param name         the name
     * @param relatedSport the related sport
     * @param minCapacity  the min capacity
     * @param maxCapacity  the max capacity
     * @param isSportHall  the is sport hall
     * @throws IllegalStateException when there is no sport of that name
     */
    public void addSportRoom(String name, String relatedSport, int minCapacity, int maxCapacity,
                             boolean isSportHall) throws IllegalStateException {

        Sport sport = sportService.getSportById(relatedSport);
        List<Sport> sports = new ArrayList<>();
        sports.add(sport);
        SportRoom sportRoom = new SportRoom(name, sports, minCapacity, maxCapacity, isSportHall);
        sportRoomRepository.save(sportRoom);
    }

    public void deleteSportRoom(Long sportRoomId) throws NoSuchElementException {
        sportRoomRepository.deleteBySportRoomId(sportRoomId);
    }

    /**
     * Rest template rest template.
     *
     * @return the rest template
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

