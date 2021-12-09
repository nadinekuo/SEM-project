package sportfacilities.controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sportfacilities.entities.Equipment;
import sportfacilities.entities.Sport;
import sportfacilities.services.EquipmentService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class EquipmentControllerTest {

    private transient final long equipmentId = 20L;
    private transient final String name = "boxingGloves";
    private transient final boolean inUse = true;
    private transient Sport box = new Sport("boxing", false, 2, 4);
    private transient final Sport relatedSport = box;
    private transient final Equipment equipment = new Equipment(name, relatedSport, inUse);
    @Mock
    private transient EquipmentService equipmentService;
    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets up the tests.
     */
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