package sportFacilitiesPackage.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sportFacilitiesPackage.entities.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {

    Optional<Equipment> findById(String equipmentName);

    @Transactional
    void deleteById(String equipmentName);

}
