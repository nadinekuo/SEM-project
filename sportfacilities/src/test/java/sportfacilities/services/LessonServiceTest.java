package sportfacilities.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import net.minidev.asm.ex.NoSuchFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import sportfacilities.entities.Lesson;
import sportfacilities.repositories.LessonRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LessonServiceTest {

    private final transient long lessonId = 0L;
    private final transient String name = "Tango";
    private final transient LocalDateTime startingTime = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
    private final transient LocalDateTime endingTime = LocalDateTime.of(2021, 1, 1, 11, 0, 0);
    private final transient int size = 10;
    private transient Lesson lesson1;
    private final long invalidId = 13;

    @Mock
    private transient LessonRepository lessonRepository;

    @InjectMocks
    private transient LessonService lessonService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    void setup() {
        lessonRepository = Mockito.mock(LessonRepository.class);
        lessonService = new LessonService(lessonRepository);
        when(lessonRepository.findById(lessonId))
            .thenReturn(java.util.Optional.of(new Lesson(name, startingTime, endingTime, size)));
        lesson1 = new Lesson(name, startingTime, endingTime, size);

        when(lessonRepository.findById(lessonId)).thenReturn(java.util.Optional.of(lesson1));
    }

    @Test
    public void ConstructorTest() {
        assertNotNull(lessonService);
    }

    @Test
    public void getLessonByIdTest() throws NoSuchFieldException {
        assertEquals(Optional.of(0L),
            Optional.of(lessonService.getLessonById(lessonId).getLessonId()));

    }

    @Test
    public void getLessonByIdThrowsExceptionTest() throws NoSuchFieldException {
        when(lessonRepository.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> lessonService.getLessonById(invalidId));
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
        assertEquals(startingTime.toString(), lessonService.getLessonStartingTime(lessonId));
    }

    @Test
    public void addNewLessonTest() throws NoSuchFieldException {
        lessonService.addNewLesson("NewLesson", startingTime, endingTime, 5);

        ArgumentCaptor<Lesson> lessonArgumentCaptor = ArgumentCaptor.forClass(Lesson.class);

        verify(lessonRepository).save(lessonArgumentCaptor.capture());

        Lesson capturedLesson = lessonArgumentCaptor.getValue();
        assertEquals(capturedLesson.getTitle(), "NewLesson");
    }

    @Test
    public void deleteLessonTest() throws NoSuchElementException {
        assertDoesNotThrow(() -> lessonService.deleteLesson(lessonId));
    }

}
