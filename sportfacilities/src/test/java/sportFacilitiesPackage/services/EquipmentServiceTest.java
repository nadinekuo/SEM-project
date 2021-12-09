package sportFacilitiesPackage.services;

import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Publisher;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.client.RestTemplate;
import sportFacilitiesPackage.entities.Equipment;
import sportFacilitiesPackage.entities.Sport;
import sportFacilitiesPackage.repositories.EquipmentRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static reactor.core.publisher.Mono.when;


@RunWith(MockitoJUnitRunner.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EquipmentServiceTest {

    Sport kickboxing = new Sport("kickbox", false, 1, -1);

    private final long equipmentId = 0L;
    private final String name = "boxingGloves";
    private final Sport relatedSport = kickboxing;
    private final boolean inUse = true;

    private Equipment equipment1;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private EquipmentService equipmentService;

    @BeforeEach
    void setup() {
        equipmentRepository = Mockito.mock(EquipmentRepository.class);
        equipmentService = new EquipmentService(equipmentRepository);
        equipment1 = new Equipment(name, relatedSport, inUse);
        equipmentRepository.save(equipment1);
    }

    @Test
    public void testConstructor() {
        assertNotNull(equipmentService);
    }

    @Test
    public void getEquipmentTest() throws NoSuchFieldException {
        Mockito.when(equipmentRepository.findByEquipmentId(equipmentId)).thenReturn(new Equipment(name,
            relatedSport, inUse));

        assertEquals(0L, equipmentService.getEquipment(equipmentId).getEquipmentId());
    }

    @Test
    public void setEquipmentToNotInUseTest() throws NoSuchFieldException {
        Mockito.when(equipmentRepository.findByEquipmentId(equipmentId)).thenReturn(new Equipment(name,
            relatedSport, true));

        equipmentService.setEquipmentToNotInUse(equipmentId);
        assertFalse(equipmentService.getEquipment(equipmentId).isInUse());
    }

    @Test
    public void setEquipmentToInUseTest() throws NoSuchFieldException {
        Mockito.when(equipmentRepository.findByEquipmentId(equipmentId)).thenReturn(new Equipment(name,
            relatedSport, false));

        equipmentService.setEquipmentToInUse(equipmentId);
        assertTrue(equipmentService.getEquipment(equipmentId).isInUse());
    }

    @Test
    public void getAvailableEquipmentIdsByNameTest() throws NoSuchFieldException {
        Equipment equipment2 = new Equipment(name, relatedSport, inUse);
        Equipment equipment3 = new Equipment(name, relatedSport, inUse);
        equipmentRepository.saveAll(List.of(equipment2, equipment3));

        Mockito.when(equipmentRepository.findAvailableEquipment(name)).thenReturn(
            java.util.Optional.of(equipmentId));

        assertEquals(java.util.Optional.of(equipment1.getEquipmentId()),
            java.util.Optional.of(equipmentService.getAvailableEquipmentIdsByName(name)));
    }

    @Test
    public void addEquipmentTest() {
        Equipment equipment2 = new Equipment(name, relatedSport, inUse);
        equipmentService.addEquipment(equipment2);
        Mockito.when(equipmentRepository.findByEquipmentId(1L)).thenReturn(equipment2);
        assertEquals(equipment2,
            equipmentRepository.findByEquipmentId(1L));
    }

    @Test
    public void restTemplateTest() {
        restTemplate = equipmentService.restTemplate();
        assertNotNull(restTemplate);
    }


}
