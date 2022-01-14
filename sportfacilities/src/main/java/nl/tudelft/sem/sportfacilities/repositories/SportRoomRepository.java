package nl.tudelft.sem.sportfacilities.repositories;

import java.util.Optional;
import nl.tudelft.sem.sportfacilities.entities.SportRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SportRoomRepository extends JpaRepository<SportRoom, String> {

    Optional<SportRoom> findBySportRoomId(Long sportRoomId);

    @Transactional
    void deleteBySportRoomId(Long sportRoomId);
}