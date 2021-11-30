package sportFacilitiesPackage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        return sportRoomRepository.findById(sportRoomId).orElseThrow();
    }

}
