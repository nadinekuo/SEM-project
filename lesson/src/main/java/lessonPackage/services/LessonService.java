package lessonPackage.services;

import lessonPackage.entities.Lesson;
import lessonPackage.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonService {


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
        return lessonRepository.findById(lessonId);
    }

}
