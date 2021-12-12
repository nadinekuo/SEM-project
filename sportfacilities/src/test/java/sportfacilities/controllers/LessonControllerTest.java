package sportfacilities.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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
import sportfacilities.services.LessonService;
import sportfacilities.services.SportService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class LessonControllerTest {

    private final transient long lessonId = 1L;
    private final transient String name = "Tango";
    private final transient LocalDateTime startingTime = LocalDateTime.of(2020, 1, 1, 10, 0, 0);
    private final transient LocalDateTime endingTime = LocalDateTime.of(2020, 1, 1, 11, 0, 0);
    private final transient int size = 10;

    @Autowired
    private transient MockMvc mockMvc;
    @Mock
    private transient LessonService lessonService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new LessonController(lessonService))
                .build();
        //equipmentService.addEquipment(equipment);
    }

    @Test
    public void getLessonTest() throws Exception {
        mockMvc.perform(get("/lesson/{lessonId}", lessonId)).andExpect(status().isOk());
        verify(lessonService).getLessonById(lessonId);
    }


    @Test
    public void getSizeTest() throws Exception {
        mockMvc.perform(get("/lesson/{lessonId}/getSize", lessonId))
            .andExpect(status().isOk());
        verify(lessonService).getLessonSize(lessonId);
    }

    @Test
    public void setLessonSizeTest() throws Exception {
        int newSize = 5;
        mockMvc.perform(post("/lesson/{lessonId}/{newSize}/setSize", lessonId, newSize))
            .andExpect(status().isOk());
        verify(lessonService).setLessonSize(lessonId, newSize);
    }

    @Test
    public void getLessonStartingTimeTest() throws Exception {
        mockMvc.perform(get("/lesson/{lessonId}/getStartingTime", lessonId))
            .andExpect(status().isOk());
        verify(lessonService).getLessonStartingTime(lessonId);
    }

    @Test
    public void createNewLessonTest() throws Exception {
        mockMvc.perform(put("/lesson/{title}/{startingTime}/{endingTime}/{size}/createNewLesson"
                    + "/admin", name,
                startingTime, endingTime, size))
            .andExpect(status().isOk());
        verify(lessonService).addNewLesson(name, startingTime, endingTime, size);
    }

}