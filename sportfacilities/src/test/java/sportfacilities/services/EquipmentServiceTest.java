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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    
    private final long id1 = 66L;
    private final long id2 = 12L;

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
        equipment1 = new Equipment(id1, "boxingGloves", kickboxing, true);
        equipment2 = new Equipment(id2, "tennisBall", tennis, false);
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

        Mockito.when(equipmentRepository.findByEquipmentId(id1))
            .thenReturn(Optional.of(equipment1));

        Equipment result = equipmentService.getEquipment(id1);

        assertThat(result.getEquipmentId()).isEqualTo(id1);
        assertThat(result.isInUse()).isTrue();
        assertThat(result.getName().equals( "boxingGloves"));
    }

    /**
     * Sets equipment to not in use test.
     */
    @Test
    public void setEquipmentToNotInUseTest() {

        Mockito.when(equipmentRepository.existsById(id1)).thenReturn(true);
        when(equipmentRepository.findByEquipmentId(id1)).thenReturn(
            Optional.ofNullable(equipment1));

        equipmentService.setEquipmentToNotInUse(id1);

        assertFalse(equipmentService.getEquipment(id1).isInUse());
    }

    /**
     * Sets equipment to in use test.
     */
    @Test
    public void setEquipmentToInUseTest() {

        Mockito.when(equipmentRepository.existsById(id2)).thenReturn(true);
        when(equipmentRepository.findByEquipmentId(id2)).thenReturn(
            Optional.ofNullable(equipment2));

        equipmentService.setEquipmentToInUse(id2);

        assertTrue(equipmentService.getEquipment(id2).isInUse());
    }

    /**
     * Gets available equipment ids by name test.
     */
    @Test
    public void getAvailableEquipmentIdsByNameTest() {

        Mockito.when(equipmentRepository.findAvailableEquipment("boxingGloves"))
            .thenReturn(java.util.Optional.of(id1));

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
            () -> equipmentService.getAvailableEquipmentIdsByName(
            "test"));
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
    public void deleteEquipmentTest()  {
        Mockito.when(equipmentRepository.findByEquipmentId(equipment1.getEquipmentId()))
            .thenReturn(Optional.of(equipment1));
        assertDoesNotThrow(() -> equipmentService.deleteEquipment(equipment1.getEquipmentId()));

    }

    @Test
    public void deleteEquipmentWithNonExistentId() throws Exception {
        doThrow(new NoSuchElementException()).when(equipmentRepository)
            .deleteByEquipmentId(id1);
        assertThrows(NoSuchElementException.class,
            () -> equipmentService.deleteEquipment(id1));
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
