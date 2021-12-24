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
            () -> new NoSuchElementException(
                "Sport room with id " + sportRoomId + " does not exist!"));
    }

    /**
     * Sets sport room min capacity.
     *
     * @param sportRoomId the sport room id
     * @param minCapacity the min capacity
     */
    public void setSportRoomMinCapacity(Long sportRoomId, int minCapacity) {
        SportRoom sportRoom = getSportRoom(sportRoomId);
        sportRoom.setMinCapacity(minCapacity);
        sportRoomRepository.save(sportRoom);
    }

    /**
     * Sets sport room max capacity.
     *
     * @param sportRoomId the sport room id
     * @param maxCapacity the max capacity
     */
    public void setSportRoomMaxCapacity(Long sportRoomId, int maxCapacity) {
        SportRoom sportRoom = getSportRoom(sportRoomId);
        sportRoom.setMaxCapacity(maxCapacity);
        sportRoomRepository.save(sportRoom);
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

    /**
     * Deletes a sport room.
     *
     * @param sportRoomId the sport room id
     * @throws NoSuchElementException if the sport room does not exist
     */
    public void deleteSportRoom(Long sportRoomId) throws NoSuchElementException {
        sportRoomRepository.deleteBySportRoomId(sportRoomId);
    }

    /**
     * Sets the sport room name.
     *
     * @param sportRoomId   the sport room id
     * @param sportRoomName the sport room name
     */
    public void setSportRoomName(Long sportRoomId, String sportRoomName) {
        SportRoom sportRoom = getSportRoom(sportRoomId);
        sportRoom.setSportRoomName(sportRoomName);
        sportRoomRepository.save(sportRoom);
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

    /**
     * Add sport to sports hall.
     *
     * @param sportRoomId the sport room id
     * @param sportName   the sport name
     */

    //TODO check for duplicates
    public void addSportToSportsHall(Long sportRoomId, String sportName) {
        Sport sport = sportService.getSportById(sportName);
        SportRoom sportHall = getSportRoom(sportRoomId);
        if (!sportHall.isSportsHall()) {
            throw new IllegalArgumentException("Only a sportHall can have multiple sports");
        }
        sportHall.addSport(sport);

        sportRoomRepository.save(sportHall);
    }
}

