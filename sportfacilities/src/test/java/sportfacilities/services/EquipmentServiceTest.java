package sportfacilities.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.Equipment;
import sportfacilities.entities.Sport;
import sportfacilities.repositories.EquipmentRepository;

/**
 * The type Equipment service test.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EquipmentServiceTest {

    private final transient Sport kickboxing;
    private final transient Sport tennis;
    private final transient Equipment equipment1;
    private final transient Equipment equipment2;

    private final long Id1 = 66L;
    private final long Id2 = 12L;

    @Mock
    private transient RestTemplate restTemplate;

    @Mock
    private transient EquipmentRepository equipmentRepository;

    private transient EquipmentService equipmentService;

    /**
     * Instantiates a new Equipment service test.
     */
    public EquipmentServiceTest() {

        kickboxing = new Sport("kickbox");
        tennis = new Sport("tennis", 4, 15);
        equipment1 = new Equipment(Id1, "boxingGloves", kickboxing, true);
        equipment2 = new Equipment(Id2, "tennisBall", tennis, false);
    }

    /**
     * Sets .
     */
    @BeforeEach
    void setup() {
        equipmentRepository = Mockito.mock(EquipmentRepository.class);
        equipmentService = new EquipmentService(equipmentRepository);
        when(equipmentRepository.findByEquipmentId(Id1))
            .thenReturn(Optional.of(equipment1));
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(equipmentService);
    }

    /**
     * Gets equipment test.
     */
    @Test
    public void getEquipmentTest() {
        Equipment result = equipmentService.getEquipment(Id1);

        assertThat(result.getEquipmentId()).isEqualTo(Id1);
        assertThat(result.isInUse()).isTrue();
        assertThat(result.getName().equals("boxingGloves"));
    }

    @Test
    public void getEquipmentThrowsExceptionTest() {
        Long invalidId = 13L;
        when(equipmentRepository.findByEquipmentId(invalidId))
            .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> equipmentService.getEquipment(invalidId));
    }

    @Test
    public void getEquipmentNameTest() {
        assertThat(equipmentService.getEquipmentName(Id1)).isEqualTo("boxingGloves");
    }

    /**
     * Sets equipment to not in use test.
     */
    @Test
    public void setEquipmentToNotInUseTest() {
        equipmentService.setEquipmentToNotInUse(Id1);
        assertFalse(equipmentService.getEquipment(Id1).isInUse());
        verify(equipmentRepository).save(equipment1);
    }

    /**
     * Sets equipment to in use test.
     */
    @Test
    public void setEquipmentToInUseTest() {
        when(equipmentRepository.findByEquipmentId(Id2))
            .thenReturn(Optional.of(equipment2));
        equipmentService.setEquipmentToInUse(Id2);

        assertTrue(equipmentService.getEquipment(Id2).isInUse());
        verify(equipmentRepository).save(equipment2);
    }

    /**
     * Gets available equipment ids by name test.
     */
    @Test
    public void getAvailableEquipmentIdsByNameTest() {

        Mockito.when(equipmentRepository.findAvailableEquipment("boxingGloves"))
            .thenReturn(java.util.Optional.of(Id1));

        assertEquals(java.util.Optional.of(equipment1.getEquipmentId()),
            java.util.Optional.of(equipmentService.getAvailableEquipmentIdsByName("boxingGloves")));
    }

    /**
     * Gets non existing equipment ids by name test.
     */
    @Test
    public void getNonExistingEquipmentIdsByNameTest() {

        Mockito.when(equipmentRepository.findAvailableEquipment(anyString()))
            .thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class,
            () -> equipmentService.getAvailableEquipmentIdsByName("test"));
    }

    /**
     * Add equipment test.
     */
    @Test
    public void addEquipmentTest() {
        equipmentService.addEquipment(equipment2);

        ArgumentCaptor<Equipment> equipmentArgumentCaptor =
            ArgumentCaptor.forClass(Equipment.class);

        verify(equipmentRepository).save(equipmentArgumentCaptor.capture());

        Equipment capturedEquipment = equipmentArgumentCaptor.getValue();
        Assertions.assertEquals(capturedEquipment.getName(), "tennisBall");
    }

    @Test
    public void deleteEquipmentTest() {
        assertDoesNotThrow(() -> equipmentService.deleteEquipment(Id1));

    }

    /**
     * Rest template test.
     */
    @Test
    public void restTemplateTest() {
        restTemplate = equipmentService.restTemplate();
        assertNotNull(restTemplate);
    }

}
