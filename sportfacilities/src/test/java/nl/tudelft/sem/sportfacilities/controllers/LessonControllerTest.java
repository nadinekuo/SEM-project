package nl.tudelft.sem.sportfacilities.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import nl.tudelft.sem.sportfacilities.services.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class LessonControllerTest {

    private final transient long lessonId = 1L;
    private final transient long invalidId = 13L;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(new LessonController(lessonService)).build();
    }

    @Test
    public void getLessonTest() throws Exception {
        mockMvc.perform(get("/lesson/{lessonId}", lessonId)).andExpect(status().isOk());
        verify(lessonService).getLessonById(lessonId);
    }

    @Test
    public void getLessonThrowsExceptionTest() throws Exception {
        when(lessonService.getLessonById(invalidId)).thenThrow(new NoSuchElementException());
        mockMvc.perform(get("/lesson/{lessonId}", invalidId)).andExpect(status().isBadRequest());

    }

    @Test
    public void getSizeTest() throws Exception {
        mockMvc.perform(get("/lesson/{lessonId}/getSize", lessonId)).andExpect(status().isOk());
        verify(lessonService).getLessonSize(lessonId);
    }

    @Test
    public void getSizeThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(lessonService).getLessonSize(invalidId);
        mockMvc.perform(get("/lesson/{lessonId}/getSize", invalidId))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void setLessonSizeTest() throws Exception {
        int newSize = 5;
        mockMvc.perform(post("/lesson/{lessonId}/{newSize}/setSize/admin", lessonId, newSize))
            .andExpect(status().isOk());
        verify(lessonService).setLessonSize(lessonId, newSize);
    }

    @Test
    public void setLessonSizeThrowsExceptionTest() throws Exception {
        int newSize = 5;

        doThrow(NoSuchElementException.class).when(lessonService).setLessonSize(invalidId, newSize);

        mockMvc.perform(post("/lesson/{lessonId}/{newSize}/setSize/admin", invalidId, newSize))
            .andExpect(status().isBadRequest());

        verify(lessonService).setLessonSize(invalidId, newSize);
    }

    @Test
    public void getLessonStartingTimeTest() throws Exception {
        mockMvc.perform(get("/lesson/{lessonId}/getStartingTime", lessonId))
            .andExpect(status().isOk());
        verify(lessonService).getLessonStartingTime(lessonId);
    }

    @Test
    public void getStartingTimeThrowsExceptionTest() throws Exception {
        when(lessonService.getLessonStartingTime(lessonId)).thenThrow(new NoSuchElementException());
        mockMvc.perform(get("/lesson/{lessonId}/getStartingTime", lessonId))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createNewLessonTest() throws Exception {
        mockMvc.perform(
            put("/lesson/{title}/{startingTime}/{endingTime}/{size}/createNewLesson" + "/admin",
                name, startingTime, endingTime, size)).andExpect(status().isOk());
        verify(lessonService).addNewLesson(name, startingTime, endingTime, size);
    }

    @Test
    public void createNewLessonThrowExceptionTest() throws Exception {
        String fakeTime = "blippBloppBlingBlong";
        mockMvc.perform(
            put("/lesson/{title}/{startingTime}/{endingTime}/{size}/createNewLesson" + "/admin",
                name, fakeTime, endingTime, size)).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteLessonTest() throws Exception {
        mockMvc.perform(delete("/lesson/{lessonId}/admin", lessonId)).andExpect(status().isOk());
        verify(lessonService).deleteLesson(lessonId);
    }

    @Test
    public void deleteLessonThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(lessonService).deleteLesson(invalidId);
        mockMvc.perform(delete("/lesson/{lessonId}/admin", invalidId))
            .andExpect(status().isBadRequest());
    }

}