package sportFacilitiesPackage.repositories;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sportFacilitiesPackage.entities.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {

    Equipment findByEquipmentId(long equipmentId);


    @Transactional
    void deleteByEquipmentId(long equipmentId);

}
