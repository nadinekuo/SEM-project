package sportFacilitiesPackage.controllers;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertEquals;

import org.aspectj.apache.bcel.classfile.Code;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sportFacilitiesPackage.entities.Equipment;
import sportFacilitiesPackage.entities.Sport;
import sportFacilitiesPackage.services.EquipmentService;
import sportFacilitiesPackage.services.SportService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    Sport kickboxing = new Sport("kickbox", false, 1, -1);

    private final long equipmentId = 20L;
    private final String name = "boxingGloves";
    private final Sport relatedSport = kickboxing;
    private final boolean inUse = true;

    //private final Equipment equipment1 = new Equipment(name, relatedSport, inUse);
    //private final Equipment equipment2 = new Equipment(name, relatedSport, false);
    //private final Equipment equipment3 = new Equipment(name, relatedSport, false);
    //private final Equipment equipment4 = new Equipment(name, relatedSport, false);

    @Mock
    EquipmentService equipmentService;

    @Mock
    SportService sportService;

    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new EquipmentController(equipmentService, sportService)).build();
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
        when(equipmentService.getAvailableEquipmentIdsByName(name)).thenThrow(new NoSuchFieldException());

        MvcResult result =
            mockMvc.perform(get("/equipment/{name}/getAvailableEquipment", name)).andExpect(status().isBadRequest())
            .andReturn();

        assertEquals(result.getResponse().getContentAsString(),"The equipment requested is not "
            + "in "
            + "stock or the "
            + "equipment name was not found");
        //verify(equipmentService, never()).getEquipment(equipmentId);

/*        Code content = result.getResponse();

        String s = content.toString();
        Assertions.assertEquals("The equipment requested is not in stock or the "
            + "equipment name was not found", content);*/
    }



    @Test
    public void addNewEquipmentTest() throws Exception {
        Equipment equipment1 = new Equipment(name, relatedSport, inUse);
        mockMvc.perform(put("/equipment/{equipmentName}/{relatedSport}/addNewEquipment/admin",
            name, relatedSport)).andExpect(status().isOk());
        verify(equipmentService).addEquipment(equipment1);
    }

    @Test
    public void equipmentBroughtBackTest() throws Exception {
        mockMvc.perform(post("/equipment/{equipmentId}/broughtBack/admin",
            equipmentId)).andExpect(status().isOk());

        verify(equipmentService).setEquipmentToNotInUse(equipmentId);
        verify(equipmentService, never()).setEquipmentToInUse(equipmentId);
    }

    @Test
    public void equipmentReservedTest() throws Exception {
        mockMvc.perform(post("/equipment/{equipmentId}/reserved",
            equipmentId)).andExpect(status().isOk());

        verify(equipmentService).setEquipmentToInUse(equipmentId);
        verify(equipmentService, never()).setEquipmentToNotInUse(equipmentId);
    }






}