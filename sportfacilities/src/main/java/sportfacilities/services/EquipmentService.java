package sportfacilities.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.Equipment;
import sportfacilities.repositories.EquipmentRepository;

/**
 * The type Equipment service.
 */
@Service
public class EquipmentService {

    private final transient EquipmentRepository equipmentRepository;

    /**
     * Instantiates a new Equipment service.
     *
     * @param equipmentRepository the equipment repository
     */
    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * Rest template rest template.
     *
     * @return the rest template
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Gets equipment.
     *
     * @param equipmentId the equipment id
     * @return the equipment
     */
    public Equipment getEquipment(Long equipmentId) {
        return equipmentRepository.findByEquipmentId(equipmentId).orElseThrow(
            () -> new IllegalStateException(
                "Equipment with id " + equipmentId + " does not exist!"));
    }

    /**
     * Gets equipment name.
     *
     * @param equipmentId the equipment id
     * @return the equipment name
     * @throws NoSuchFieldException the no such field exception
     */
    public String getEquipmentName(Long equipmentId) throws NoSuchFieldException {
        return equipmentRepository.findByEquipmentId(equipmentId).get().getName();
    }

    /**
     * Sets equipment to not in use.
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentToNotInUse(Long equipmentId) {

        boolean exists = equipmentRepository.existsById(equipmentId);
        if (!exists) {
            throw new IllegalStateException(
                "Equipment with id " + equipmentId + " does not " + "exist!");
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
            throw new IllegalStateException(
                "Equipment with id " + equipmentId + " does not " + "exist!");
        }
        Equipment equipment = equipmentRepository.findByEquipmentId(equipmentId).get();
        equipment.setInUse(true);
        equipmentRepository.save(equipment);
    }

    /**
     * Gets available equipment ids by name.
     *
     * @param equipmentName the equipment name
     * @return the available equipment ids by name
     */
    public long getAvailableEquipmentIdsByName(String equipmentName) {
        Optional<Long> res = equipmentRepository.findAvailableEquipment(equipmentName);
        if (res.isPresent()) {
            return res.get();
        } else {
            System.out.println("Equipment " + equipmentName + " does not exist / is unavailable!");
            return -1L;
        }
    }

    /**
     * Add equipment.
     *
     * @param equipment the equipment
     */
    public void addEquipment(Equipment equipment) {
        equipmentRepository.save(equipment);
    }
}
