package sportfacilities.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import net.minidev.asm.ex.NoSuchFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.Lesson;
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

    /**
     * Sets up the tests .
     */
    @BeforeEach
    void setup() {
        lessonRepository = Mockito.mock(LessonRepository.class);
        lessonService = new LessonService(lessonRepository);
        when(lessonRepository.findById(lessonId)).thenReturn(
            java.util.Optional.of(new Lesson(name, startingTime, endingTime, size)));
        lesson1 = new Lesson(name, startingTime, endingTime, size);
        lessonRepository.save(lesson1);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(lessonService);
    }

    /**
     * Gets lesson by id test.
     *
     * @throws NoSuchFieldException the no such field exception
     */
    @Test
    public void getLessonByIdTest() throws NoSuchFieldException {
        assertEquals(Optional.of(0L),
            Optional.of(lessonService.getLessonById(lessonId).getLessonId()));
    }

    /**
     * Sets lesson size test.
     *
     * @throws NoSuchFieldException the no such field exception
     */
    @Test
    public void setLessonSizeTest() throws NoSuchFieldException {
        int newSize = 5;
        lessonService.setLessonSize(lessonId, newSize);
        assertEquals(newSize, lessonService.getLessonSize(lessonId));
    }

    /**
     * Gets lesson size test.
     *
     * @throws NoSuchFieldException the no such field exception
     */
    @Test
    public void getLessonSizeTest() throws NoSuchFieldException {
        assertEquals(size, lessonService.getLessonSize(lessonId));
    }

    /**
     * Gets lesson starting time test.
     *
     * @throws NoSuchFieldException the no such field exception
     */
    @Test
    public void getLessonStartingTimeTest() throws NoSuchFieldException {
        assertEquals(startingTime.toString(), lessonService.getLessonStartingTime(lessonId));
    }

    /**
     * Add new lesson test.
     *
     * @throws NoSuchFieldException the no such field exception
     */
    @Test
    public void addNewLessonTest() throws NoSuchFieldException {
        Lesson lesson2 = new Lesson("NewLesson", startingTime, endingTime, 5);
        lessonService.addNewLesson("NewLesson", startingTime, endingTime, 5);
        when(lessonRepository.findById(1L)).thenReturn(java.util.Optional.of(lesson2));
        assertEquals("NewLesson", lessonService.getLessonById(1L).getTitle());
    }

    /**
     * Delete lesson test.
     *
     * @throws NoSuchElementException the no such element exception
     */
    @Test
    public void deleteLessonTest() throws NoSuchElementException {
        assertThrows(NoSuchElementException.class,() -> lessonService.deleteLesson(1000L));
    }

    /**
     * Delete lesson that not exists test.
     */
    @Test
    public void deleteLessonThatNotExistsTest() {
        doThrow(new NoSuchElementException()).when(lessonRepository).deleteById(lessonId);
        assertThrows(NoSuchElementException.class, () -> lessonService.deleteLesson(lessonId));
    }
}
