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

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    public Equipment getEquipment(Long equipmentId) {
        return equipmentRepository.findByEquipmentId(equipmentId)
            .orElseThrow(() -> new IllegalStateException("Equipment with id "
                + equipmentId + " does not exist!"));
    }

    /**
     * Sets equipment to not in use.
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentToNotInUse(Long equipmentId) {

        boolean exists = equipmentRepository.existsById(equipmentId);
        if (!exists) {
            throw new IllegalStateException("Equipment with id " + equipmentId + " does not "
                + "exist!");
        }
        Equipment equipment = equipmentRepository.findByEquipmentId(equipmentId).get();
        equipment.setInUse(false);
        equipmentRepository.save(equipment);
    }

    /**
     * Sets equipment to in use.
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentToInUse(Long equipmentId) {
        boolean exists = equipmentRepository.existsById(equipmentId);
        if (!exists) {
            throw new IllegalStateException("Equipment with id " + equipmentId + " does not "
                + "exist!");
        }
        Equipment equipment = equipmentRepository.findByEquipmentId(equipmentId).get();
        equipment.setInUse(true);
        equipmentRepository.save(equipment);
    }


    public Long getAvailableEquipmentIdsByName(String equipmentName) {
        return equipmentRepository.findAvailableEquipment(equipmentName)
            .orElseThrow(() -> new IllegalStateException("Equipment with id "
            + equipmentName + " does not exist!"));
    }


    public void addEquipment(Equipment equipment) {
        equipmentRepository.save(equipment);
    }
}
