package sportfacilities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.SportRoom;
import sportfacilities.repositories.SportRoomRepository;

/**
 * The type Sport room service.
 */
@Service
public class SportRoomService {
    private final transient SportRoomRepository sportRoomRepository;

    /**
     * Instantiates a new Sport room service.
     *
     * @param sportRoomRepository the sport room repository
     */
    @Autowired
    public SportRoomService(SportRoomRepository sportRoomRepository) {
        this.sportRoomRepository = sportRoomRepository;
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
