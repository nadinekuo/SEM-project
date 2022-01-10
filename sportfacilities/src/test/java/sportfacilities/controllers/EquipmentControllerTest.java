package sportfacilities.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sportfacilities.entities.Equipment;
import sportfacilities.entities.Sport;
import sportfacilities.services.EquipmentService;
import sportfacilities.services.SportService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class EquipmentControllerTest {

    private final transient long equipmentId = 20L;
    private final transient long invalidId = 13L;
    private final transient String equipmentName = "boxingGloves";
    private final transient boolean inUse = true;
    private final transient Sport box = new Sport("boxing", 2, 4);
    private final transient Equipment equipment1 = new Equipment(equipmentName, box, inUse);

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
    }

    @Test
    public void getEquipmentTest() throws Exception {
        mockMvc.perform(get("/equipment/{equipmentId}", equipmentId)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
        verify(equipmentService).getEquipment(equipmentId);
    }

    @Test
    public void getEquipmentNameTest() throws Exception {
        mockMvc.perform(get("/equipment/{equipmentId}/getEquipmentName", equipmentId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(equipmentService).getEquipmentName(equipmentId);
    }

    @Test
    public void getEquipmentNameThrowsExceptionTest() throws Exception {
        when(equipmentService.getEquipmentName(invalidId)).thenThrow(new NoSuchElementException());
        mockMvc.perform(get("/equipment/{equipmentId}/getEquipmentName", invalidId))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getEquipmentWithNotValidIdTest() throws Exception {
        when(equipmentService.getEquipment(10L)).thenThrow(new NoSuchElementException());

        MvcResult result = mockMvc.perform(get("/equipment/10")).andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print()).andReturn();

        verify(equipmentService).getEquipment(10L);

        String content = result.getResponse().getContentType();
        Assertions.assertNull(content);
    }

    @Test
    public void getAvailableEquipmentTest() throws Exception {
        mockMvc.perform(get("/equipment/{name}/getAvailableEquipment", equipmentName))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(equipmentService).getAvailableEquipmentIdsByName(equipmentName);
    }

    @Test
    public void getAvailableEquipmentThrowExceptionTest() throws Exception {
        String name = "eSports";
        when(equipmentService.getAvailableEquipmentIdsByName(name)).thenThrow(
            new NoSuchElementException());
        mockMvc.perform(get("/equipment/{name}/getAvailableEquipment", name))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void addNewEquipmentTest() throws Exception {
        Equipment equipment1 = new Equipment(equipmentName, box, inUse);
        mockMvc.perform(
            put("/equipment/{equipmentName}/{relatedSport}/addNewEquipment/admin", equipmentName,
                box)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(equipmentService).addEquipment(equipment1);
    }

    @Test
    public void addNewEquipmentThrowsExceptionTest() throws Exception {
        String invalidSport = "Golf";
        when(sportService.getSportById(invalidSport)).thenThrow(new NoSuchElementException());
        mockMvc.perform(
                put("/equipment/{equipmentName}/{relatedSport}/addNewEquipment/admin",
                    equipmentName,
                    invalidSport)).andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void deleteEquipmentTest() throws Exception {

        mockMvc.perform(delete("/equipment/{equipmentId}/deleteEquipment/admin", equipmentId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(equipmentService).deleteEquipment(equipmentId);
    }

    @Test
    public void deleteEquipmentThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(equipmentService).deleteEquipment(invalidId);
        mockMvc.perform(delete("/equipment/{equipmentId}/deleteEquipment/admin", invalidId))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void equipmentBroughtBackTest() throws Exception {
        mockMvc.perform(put("/equipment/{equipmentId}/broughtBack/admin", equipmentId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

        verify(equipmentService).setEquipmentToNotInUse(equipmentId);
        verify(equipmentService, never()).setEquipmentToInUse(equipmentId);
    }

    @Test
    public void equipmentBroughtBackThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(equipmentService)
            .setEquipmentToNotInUse(invalidId);
        mockMvc.perform(put("/equipment/{equipmentId}/broughtBack/admin", invalidId))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void equipmentReservedTest() throws Exception {
        mockMvc.perform(put("/equipment/{equipmentId}/reserved", equipmentId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

        verify(equipmentService).setEquipmentToInUse(equipmentId);
        verify(equipmentService, never()).setEquipmentToNotInUse(equipmentId);
    }

    @Test
    public void equipmentReservedThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(equipmentService).setEquipmentToInUse(invalidId);
        mockMvc.perform(put("/equipment/{equipmentId}/reserved", invalidId))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }
}