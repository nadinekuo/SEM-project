package sportFacilitiesPackage.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sportFacilitiesPackage.entities.SportRoom;

@Repository
public interface SportRoomRepository extends JpaRepository<SportRoom, String> {

    Optional<SportRoom> findById(String sportRoomId);

    @Transactional
    void deleteById(String sportRoomId);
}