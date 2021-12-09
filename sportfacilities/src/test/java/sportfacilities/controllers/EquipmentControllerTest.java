package sportfacilities.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sportfacilities.entities.Equipment;
import sportfacilities.entities.Sport;
import sportfacilities.services.EquipmentService;
import sportfacilities.services.SportService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class EquipmentControllerTest {

    private final transient long equipmentId = 20L;
    private final transient String name = "boxingGloves";
    private final transient boolean inUse = true;
    private final transient Sport box = new Sport("boxing", false, 2, 4);
    private final transient Sport relatedSport = box;
    private final transient Equipment equipment = new Equipment(name, relatedSport, inUse);
    @Mock
    transient SportService sportService;
    @Autowired
    private transient MockMvc mockMvc;
    @Mock
    private transient EquipmentService equipmentService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new EquipmentController(equipmentService, sportService))
                .build();
        //equipmentService.addEquipment(equipment);
    }

    @Test
    public void getEquipmentTest() throws Exception {
        mockMvc.perform(get("/equipment/{equipmentId}", equipmentId)).andExpect(status().isOk());
        verify(equipmentService).getEquipment(equipmentId);
        //when(equipment1)
    }

    @Test
    public void getEquipmentWithNotValidIdTest() throws Exception {
        when(equipmentService.getEquipment(10L)).thenThrow(new NoSuchFieldException());

        MvcResult result =
            mockMvc.perform(get("/equipment/10")).andExpect(status().isOk()).andReturn();
        //mockMvc.perform(get("/equipment/10").;

        verify(equipmentService).getEquipment(10L);

        String content = result.getResponse().getContentType();
        Assertions.assertNull(content);
    }

    @Test
    public void getAvailableEquipmentTest() throws Exception {
        mockMvc.perform(get("/equipment/{name}/getAvailableEquipment", name))
            .andExpect(status().isOk());
        verify(equipmentService).getAvailableEquipmentIdsByName(name);
    }

    @Test
    public void getAvailableEquipmentBadRequestTest() throws Exception {
        when(equipmentService.getAvailableEquipmentIdsByName(name)).thenThrow(
            new NoSuchFieldException());

        MvcResult result = mockMvc.perform(get("/equipment/{name}/getAvailableEquipment", name))
            .andExpect(status().isBadRequest()).andReturn();

        assertEquals(result.getResponse().getContentAsString(),
            "The equipment requested is not in stock or the "
                + "equipment name was not found");
    }

    @Test
    public void addNewEquipmentTest() throws Exception {
        Equipment equipment1 = new Equipment(name, relatedSport, inUse);
        mockMvc.perform(put("/equipment/{equipmentName}/{relatedSport}/addNewEquipment/admin", name,
            relatedSport)).andExpect(status().isOk());
        verify(equipmentService).addEquipment(equipment1);
    }

    @Test
    public void equipmentBroughtBackTest() throws Exception {
        mockMvc.perform(post("/equipment/{equipmentId}/broughtBack/admin", equipmentId))
            .andExpect(status().isOk());

        verify(equipmentService).setEquipmentToNotInUse(equipmentId);
        verify(equipmentService, never()).setEquipmentToInUse(equipmentId);
    }

    @Test
    public void equipmentReservedTest() throws Exception {
        mockMvc.perform(post("/equipment/{equipmentId}/reserved", equipmentId))
            .andExpect(status().isOk());

        verify(equipmentService).setEquipmentToInUse(equipmentId);
        verify(equipmentService, never()).setEquipmentToNotInUse(equipmentId);
    }
}