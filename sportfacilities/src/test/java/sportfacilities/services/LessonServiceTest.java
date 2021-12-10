package sportfacilities.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import net.minidev.asm.ex.NoSuchFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.Equipment;
import sportfacilities.entities.Lesson;
import sportfacilities.entities.Sport;
import sportfacilities.repositories.EquipmentRepository;
import sportfacilities.repositories.LessonRepository;

@RunWith(MockitoJUnitRunner.class)
public class LessonServiceTest {

    private final transient long lessonId = 0L;
    private final transient String name = "Tango";
    private final transient LocalDateTime startingTime = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
    private final transient LocalDateTime endingTime = LocalDateTime.of(2021, 1, 1, 11, 0, 0);
    private final transient int size = 10;
    private transient Lesson lesson1;

    @Mock
    private transient RestTemplate restTemplate;

    @Mock
    private transient LessonRepository lessonRepository;

    @InjectMocks
    private transient LessonService lessonService;

    @BeforeEach
    void setup() {
        lessonRepository = Mockito.mock(LessonRepository.class);
        lessonService = new LessonService(lessonRepository);
        Mockito.when(lessonRepository.findById(lessonId))
            .thenReturn(java.util.Optional.of(new Lesson(name, startingTime, endingTime, size)));
        lesson1 = new Lesson(name, startingTime, endingTime, size);
        lessonRepository.save(lesson1);
    }

    @Test
    public void testConstructor() {
        assertNotNull(lessonService);
    }

    @Test
    public void getLessonByIdTest() throws NoSuchFieldException {
        assertEquals(0L, lessonService.getLessonById(lessonId).getLessonId());
    }

    @Test
    public void setLessonSizeTest() throws NoSuchFieldException {
        int newSize = 5;
        lessonService.setLessonSize(lessonId, newSize);
        assertEquals(newSize, lessonService.getLessonSize(lessonId));
    }

    @Test
    public void getLessonSizeTest() throws NoSuchFieldException {
        assertEquals(size, lessonService.getLessonSize(lessonId));
    }

    @Test
    public void getLessonStartingTimeTest() throws NoSuchFieldException {
        assertEquals(startingTime, lessonService.getLessonStartingTime(lessonId));
    }

    @Test
    public void addNewLessonTest() throws NoSuchFieldException {
        Lesson lesson2 = new Lesson("NewLesson", startingTime, endingTime, 5);
        lessonService.addNewLesson("NewLesson", startingTime, endingTime, 5);
        Mockito.when(lessonRepository.findById(1L))
            .thenReturn(java.util.Optional.of(lesson2));
        assertEquals("NewLesson", lessonService.getLessonById(1L).getTitle());
    }
}
