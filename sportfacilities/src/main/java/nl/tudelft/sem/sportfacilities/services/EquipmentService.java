package nl.tudelft.sem.sportfacilities.services;

import java.util.NoSuchElementException;
import nl.tudelft.sem.sportfacilities.entities.Equipment;
import nl.tudelft.sem.sportfacilities.repositories.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
            () -> new NoSuchElementException(
                "Equipment with id " + equipmentId + " does not exist!"));
    }

    /**
     * Gets equipment name.
     *
     * @param equipmentId the equipment id
     * @return the equipment name
     * @throws NoSuchElementException the no such field exception
     */
    public String getEquipmentName(Long equipmentId) {
        Equipment equipment = getEquipment(equipmentId);
        return equipment.getName();
    }

    /**
     * Sets equipment to not in use.
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentToNotInUse(Long equipmentId) {
        Equipment equipment = getEquipment(equipmentId);
        equipment.setInUse(false);
        equipmentRepository.save(equipment);
    }

    /**
     * Sets equipment to in use.
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentToInUse(Long equipmentId) {
        Equipment equipment = getEquipment(equipmentId);
        equipment.setInUse(true);
        equipmentRepository.save(equipment);
    }

    /**
     * Gets available equipment ids by name.
     *
     * @param equipmentName the equipment name
     * @return the available equipment ids by name
     */

    public Long getAvailableEquipmentIdsByName(String equipmentName) {

        return equipmentRepository.findAvailableEquipment(equipmentName).orElseThrow(
            () -> new NoSuchElementException("Currently this equipment is fully booked"));

    }

    /**
     * Add equipment.
     *
     * @param equipment the equipment
     */
    public void addEquipment(Equipment equipment) {
        equipmentRepository.save(equipment);
    }

    public void deleteEquipment(long equipmentId) {
        Equipment equipment = getEquipment(equipmentId);
        equipmentRepository.deleteByEquipmentId(equipment.getEquipmentId());
    }
}
