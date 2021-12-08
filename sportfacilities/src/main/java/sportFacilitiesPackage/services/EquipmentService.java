package sportFacilitiesPackage.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sportFacilitiesPackage.entities.Equipment;
import sportFacilitiesPackage.entities.SportRoom;
import sportFacilitiesPackage.repositories.EquipmentRepository;
import sportFacilitiesPackage.repositories.SportRoomRepository;

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

    public String getEquipmentName(Long equipmentId) throws NoSuchFieldException {
        return equipmentRepository.findByEquipmentId(equipmentId).getName();
    }

    public void setEquipmentToNotInUse(Long equipmentId) {
        Equipment equipment = equipmentRepository.findByEquipmentId(equipmentId);
        equipment.setInUse(false);
        equipmentRepository.save(equipment);
    }

    public void setEquipmentToInUse(Long equipmentId) {
        Equipment equipment = equipmentRepository.findByEquipmentId(equipmentId);
        equipment.setInUse(true);
        equipmentRepository.save(equipment);
    }

    public Long getAvailableEquipmentIdsByName(String equipmentName) throws NoSuchFieldException {
        return equipmentRepository.findAvailableEquipment(equipmentName).orElseThrow();
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
