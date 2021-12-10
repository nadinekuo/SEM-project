package sportfacilities.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.Equipment;
import sportfacilities.repositories.EquipmentRepository;

@Service
public class EquipmentService {

    private final transient EquipmentRepository equipmentRepository;

    /**
     * Constructor for UserService.
     *
     * @param equipmentRepository - retrieves Equipment from database.
     */
    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public Equipment getEquipment(Long equipmentId) throws NoSuchFieldException {
        return equipmentRepository.findByEquipmentId(equipmentId);
    }

    /**
     * Sets equipment to not in use.
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentToNotInUse(Long equipmentId) {
        Equipment equipment = equipmentRepository.findByEquipmentId(equipmentId);
        equipment.setInUse(false);
        equipmentRepository.save(equipment);
    }

    /**
     * Sets equipment to in use.
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentToInUse(Long equipmentId) {
        Equipment equipment = equipmentRepository.findByEquipmentId(equipmentId);
        equipment.setInUse(true);
        equipmentRepository.save(equipment);
    }


    public Long getAvailableEquipmentIdsByName(String equipmentName) {
        Optional<Long> potentialEquipmentId =
            equipmentRepository.findAvailableEquipment(equipmentName);
        if (potentialEquipmentId.isPresent()) {
            return potentialEquipmentId.get();
        } else {
            return -1L;
        }
    }



    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public void addEquipment(Equipment equipment) {
        equipmentRepository.save(equipment);
    }
}
