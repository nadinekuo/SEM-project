package sportFacilitiesPackage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportFacilitiesPackage.entities.Lesson;
import sportFacilitiesPackage.repositories.LessonRepository;

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
        return lessonRepository.findById(lessonId).orElseThrow();
    }

}
