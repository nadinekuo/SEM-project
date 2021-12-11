package sportfacilities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.SportRoom;
import sportfacilities.repositories.SportRoomRepository;

@Service
public class SportRoomService {
    private final transient SportRoomRepository sportRoomRepository;

    /**
     * Constructor for UserService.
     *
     * @param sportRoomRepository - retrieves Lessons from database.
     */
    @Autowired
    public SportRoomService(SportRoomRepository sportRoomRepository) {
        this.sportRoomRepository = sportRoomRepository;
    }

    /**
     * @param sportRoomId - id
     * @return - SportRoom
     */
    public SportRoom getSportRoom(Long sportRoomId) {
        return sportRoomRepository.findBySportRoomId(sportRoomId)
            .orElseThrow(() -> new IllegalStateException("Sport room with id "
                    + sportRoomId + " does not exist!"));
    }

    /**
     * @param sportRoomId - id
     * @return boolean, true if id is valid
     */
    public Boolean sportRoomExists(Long sportRoomId) {
        return sportRoomRepository.findBySportRoomId(sportRoomId).isPresent();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



}
