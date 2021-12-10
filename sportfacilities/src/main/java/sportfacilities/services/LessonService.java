package sportfacilities.services;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportfacilities.entities.Lesson;
import sportfacilities.repositories.LessonRepository;

@Service
public class LessonService {

    @Autowired
    private final transient LessonRepository lessonRepository;

    /**
     * Constructor for UserService.
     *
     * @param lessonRepository - retrieves Lessons from database.
     */
    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Lesson getLessonById(long lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow();
    }

    public void addNewLesson(String title, LocalDateTime startTime, LocalDateTime endTime,
                             int size) {
        lessonRepository.save(new Lesson(title, startTime, endTime, size));
    }

    public void setLessonSize(long lessonId, int size) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setSize(size);
        lessonRepository.save(lesson);
    }

    public int getLessonSize(long lessonId) {
        return lessonRepository.findById(lessonId).get().getSize();
    }

    public String getLessonStartingTime(long lessonId) {
        return lessonRepository.findById(lessonId).get().getStartingTime().toString();
    }
}