package sportFacilitiesPackage.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sportFacilitiesPackage.entities.Sport;

@Repository
public interface SportRepository extends JpaRepository<Sport, String> {

    //Optional<Sport> findById(String sportName);

    @Transactional
    void deleteById(String sportName);
}