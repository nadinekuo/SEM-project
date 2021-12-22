package sportfacilities.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.Equipment;
import sportfacilities.entities.Sport;
import sportfacilities.repositories.EquipmentRepository;

/**
 * The type Equipment service test.
 */
@RunWith(MockitoJUnitRunner.class)
public class EquipmentServiceTest {

    private final transient Sport kickboxing;
    private final transient Sport tennis;
    private final transient Equipment equipment1;
    private final transient Equipment equipment2;

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
        equipment1 = new Equipment(66L, "boxingGloves", kickboxing, true);
        equipment2 = new Equipment(12L, "tennisBall", tennis, false);
    }

    /**
     * Sets .
     */
    @BeforeEach
    void setup() {
        equipmentRepository = Mockito.mock(EquipmentRepository.class);
        equipmentService = new EquipmentService(equipmentRepository);
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

        Mockito.when(equipmentRepository.findByEquipmentId(66L))
            .thenReturn(Optional.of(equipment1));

        Equipment result = equipmentService.getEquipment(66L);

        assertThat(result.getEquipmentId()).isEqualTo(66L);
        assertThat(result.isInUse()).isTrue();
        assertThat(result.getName() == "boxingGloves");
    }

    /**
     * Sets equipment to not in use test.
     */
    @Test
    public void setEquipmentToNotInUseTest() {

        Mockito.when(equipmentRepository.existsById(66L)).thenReturn(true);
        when(equipmentRepository.findByEquipmentId(66L)).thenReturn(
            Optional.ofNullable(equipment1));

        equipmentService.setEquipmentToNotInUse(66L);

        assertFalse(equipmentService.getEquipment(66L).isInUse());
    }

    /**
     * Sets equipment to in use test.
     */
    @Test
    public void setEquipmentToInUseTest() {

        Mockito.when(equipmentRepository.existsById(12L)).thenReturn(true);
        when(equipmentRepository.findByEquipmentId(12L)).thenReturn(
            Optional.ofNullable(equipment2));

        equipmentService.setEquipmentToInUse(12L);

        assertTrue(equipmentService.getEquipment(12L).isInUse());
    }

    /**
     * Gets available equipment ids by name test.
     */
    @Test
    public void getAvailableEquipmentIdsByNameTest() {

        Mockito.when(equipmentRepository.findAvailableEquipment("boxingGloves"))
            .thenReturn(java.util.Optional.of(66L));

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

        assertEquals(-1L, equipmentService.getAvailableEquipmentIdsByName("test"));
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
    public void deleteSportRoomTest() throws Exception {
        doNothing().when(equipmentRepository).deleteByEquipmentId(equipment1.getEquipmentId());
        assertDoesNotThrow(() -> equipmentService.deleteEquipment(equipment1.getEquipmentId()));
    }

    @Test
    public void deleteEquipmentWithNonExistentId() throws Exception {
        doThrow(new NoSuchElementException()).when(equipmentRepository)
            .deleteByEquipmentId(equipment1.getEquipmentId());
        assertThrows(NoSuchElementException.class,
            () -> equipmentService.deleteEquipment(equipment1.getEquipmentId()));
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
