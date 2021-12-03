package sportFacilitiesPackage.controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sportFacilitiesPackage.entities.Equipment;
import sportFacilitiesPackage.entities.Sport;
import sportFacilitiesPackage.services.EquipmentService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    Sport box = new Sport("boxing", false, 2, 4);

    private final long equipmentId = 20L;
    private final String name = "boxingGloves";
    private final Sport relatedSport = box;
    private final boolean inUse = true;

    private final Equipment equipment = new Equipment(1L, name, relatedSport, inUse);

    @Mock
    EquipmentService equipmentService;

    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new EquipmentController(equipmentService)).build();
        //equipmentService.addEquipment(equipment);
    }

    @Test
    public void getEquipmentTest() throws Exception {

        mockMvc.perform(get("/equipment/{equipmentId}", equipmentId)).andExpect(status().isOk());
        verify(equipmentService).getEquipment(equipmentId);
    }




}