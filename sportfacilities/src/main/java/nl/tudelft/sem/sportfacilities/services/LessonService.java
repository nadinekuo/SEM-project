package nl.tudelft.sem.sportfacilities.services;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import nl.tudelft.sem.sportfacilities.entities.Lesson;
import nl.tudelft.sem.sportfacilities.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            () -> new NoSuchElementException("Lesson with id " + lessonId + " does not exist!"));
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
        Lesson lesson = getLessonById(lessonId);
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
        Lesson lesson = getLessonById(lessonId);
        return lesson.getSize();
    }

    /**
     * Gets lesson starting time.
     *
     * @param lessonId the lesson id
     * @return the lesson starting time
     */
    public String getLessonStartingTime(long lessonId) {
        Lesson lesson = getLessonById(lessonId);
        return lesson.getStartingTime().toString();
    }

    /**
     * Delete lesson.
     *
     * @param lessonId the lesson id
     */
    public void deleteLesson(long lessonId) throws NoSuchElementException {
        Lesson lesson = getLessonById(lessonId);
        lessonRepository.deleteById(lesson.getLessonId());
    }
}