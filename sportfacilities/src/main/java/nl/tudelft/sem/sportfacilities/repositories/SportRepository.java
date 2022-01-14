package nl.tudelft.sem.sportfacilities.repositories;

import java.util.Optional;
import nl.tudelft.sem.sportfacilities.entities.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SportRepository extends JpaRepository<Sport, String> {

    Optional<Sport> findById(String sportName);

    @Transactional
    void deleteById(String sportName);
}