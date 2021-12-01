package sportFacilitiesPackage.services;

import java.util.List;
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

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

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





    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();

    }

}
