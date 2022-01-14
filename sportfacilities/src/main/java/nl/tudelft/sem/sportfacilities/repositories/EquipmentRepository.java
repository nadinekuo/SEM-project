package nl.tudelft.sem.sportfacilities.repositories;

import java.util.Optional;
import nl.tudelft.sem.sportfacilities.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    Optional<Equipment> findByEquipmentId(Long equipmentId);

    @Transactional
    void deleteByEquipmentId(Long equipmentId);

    @Query(value = "SELECT equipment_id " + "FROM equipment " + "WHERE name = ?1 AND NOT in_use "
        + "LIMIT 1", nativeQuery = true)
    Optional<Long> findAvailableEquipment(String equipmentName);

}
