package sportfacilities.services;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportfacilities.entities.Lesson;
import sportfacilities.repositories.LessonRepository;

/**
 * The type Lesson service.
 */
@Service
public class LessonService {

    @Autowired
    private final transient LessonRepository lessonRepository;

    /**
     * Instantiates a new Lesson service.
     *
     * @param lessonRepository the lesson repository
     */
    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    /**
     * Gets lesson by id.
     *
     * @param lessonId the lesson id
     * @return the lesson by id
     */
    public Lesson getLessonById(long lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(
            () -> new NoSuchElementException("Lesson with id " + lessonId + "does not exist!"));
    }

    /**
     * Add new lesson.
     *
     * @param title     the title
     * @param startTime the start time
     * @param endTime   the end time
     * @param size      the size
     */
    public void addNewLesson(String title, LocalDateTime startTime, LocalDateTime endTime,
                             int size) {
        lessonRepository.save(new Lesson(title, startTime, endTime, size));
    }

    /**
     * Sets lesson size.
     *
     * @param lessonId the lesson id
     * @param size     the size
     */
    public void setLessonSize(long lessonId, int size) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
            () -> new NoSuchElementException("Lesson with id " + lessonId + "does not exist!"));
        lesson.setSize(size);
        lessonRepository.save(lesson);
    }

    /**
     * Gets lesson size.
     *
     * @param lessonId the lesson id
     * @return the lesson size
     */
    public int getLessonSize(long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
            () -> new NoSuchElementException("Lesson with id " + lessonId + "does not exist!"));
        return lesson.getSize();
    }

    /**
     * Gets lesson starting time.
     *
     * @param lessonId the lesson id
     * @return the lesson starting time
     */
    public String getLessonStartingTime(long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
            () -> new NoSuchElementException("Lesson with id " + lessonId + "does not exist!"));
        return lesson.getStartingTime().toString();
    }

    /**
     * Delete lesson.
     *
     * @param lessonId the lesson id
     */
    public void deleteLesson(long lessonId) throws NoSuchElementException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
            () -> new NoSuchElementException("Lesson with id " + lessonId + "does not exist!"));
        lessonRepository.deleteById(lesson.getLessonId());
    }
}