package sportFacilitiesPackage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sportFacilitiesPackage.entities.SportRoom;
import sportFacilitiesPackage.repositories.SportRoomRepository;

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

    public SportRoom getSportRoom(Long sportRoomId) throws NoSuchFieldException {
        return sportRoomRepository.findBySportRoomId(sportRoomId).orElseThrow();
    }

    public Boolean sportRoomExists(Long sportRoomId) {
        return sportRoomRepository.findBySportRoomId(sportRoomId).isPresent();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



}
