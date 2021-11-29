package sportFacilitiesPackage.services;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportFacilitiesPackage.entities.Lesson;
import sportFacilitiesPackage.entities.Sport;
import sportFacilitiesPackage.entities.SportRoom;
import sportFacilitiesPackage.repositories.LessonRepository;
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

    public SportRoom getSportRoomByName(String sportRoomName) throws NoSuchFieldException {
        return sportRoomRepository.findById(sportRoomName).orElseThrow();
    }

}
