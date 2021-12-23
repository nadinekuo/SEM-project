package sportfacilities.controllers;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sportfacilities.entities.Lesson;
import sportfacilities.services.LessonService;

/**
 * The type Lesson controller.
 */
@RestController
@RequestMapping("lesson")
public class LessonController {

    private final transient LessonService lessonService;

    /**
     * Instantiates a new Lesson controller.
     *
     * @param lessonService the lesson service
     */
    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    /**
     * Gets lesson.
     *
     * @param lessonId the lesson id
     * @return the lesson
     */
    @GetMapping("/{lessonId}")
    @ResponseBody
    public Lesson getLesson(@PathVariable long lessonId) {
        return lessonService.getLessonById(lessonId);
    }

    /**
     * Gets lesson size.
     *
     * @param lessonId the lesson id
     * @return the lesson size
     */
    @GetMapping("/{lessonId}/getSize")
    @ResponseBody
    public int getLessonSize(@PathVariable long lessonId) {
        return lessonService.getLessonSize(lessonId);
    }

    /**
     * Sets lesson size.
     *
     * @param lessonId the lesson id
     * @param size     the size
     */
    @PostMapping("/{lessonId}/{size}/setSize/admin")
    @ResponseBody
    public void setLessonSize(@PathVariable long lessonId, @PathVariable int size) {
        lessonService.setLessonSize(lessonId, size);
    }

    /**
     * Gets lesson starting time.
     *
     * @param lessonId the lesson id
     * @return the lesson starting time
     */
    @GetMapping("/{lessonId}/getStartingTime")
    @ResponseBody
    public String getLessonStartingTime(@PathVariable long lessonId) {
        return lessonService.getLessonStartingTime(lessonId);
    }

    /**
     * Create new lesson.
     *
     * @param title        the title
     * @param startingTime the starting time
     * @param endingTime   the ending time
     * @param size         the size
     */
    @PutMapping("/{title}/{startingTime}/{endingTime}/{size}/createNewLesson/admin")
    @ResponseBody
    public void createNewLesson(@PathVariable String title, @PathVariable String startingTime,
                                @PathVariable String endingTime, @PathVariable int size) {

        LocalDateTime startTime = LocalDateTime.parse(startingTime);
        LocalDateTime endTime = LocalDateTime.parse(endingTime);

        lessonService.addNewLesson(title, startTime, endTime, size);
    }

    /**
     * Delete lesson.
     *
     * @param lessonId the lesson id
     */
    @DeleteMapping("/{lessonId}/admin")
    @ResponseBody
    public void deleteLesson(@PathVariable long lessonId) {
        lessonService.deleteLesson(lessonId);
    }
}