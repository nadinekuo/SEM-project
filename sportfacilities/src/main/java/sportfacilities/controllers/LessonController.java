package sportfacilities.controllers;

import java.time.LocalDateTime;
import javax.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sportfacilities.entities.Lesson;
import sportfacilities.services.LessonService;

@RestController
@RequestMapping("lesson")
public class LessonController {

    private final transient LessonService lessonService;

    /**
     * Autowired constructor for the class.
     *
     * @param lessonService userService
     */
    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    /**
     * GET mapping.
     *
     * @param lessonId the id of the required lesson
     * @return a lesson with a specific id
     */
    @GetMapping("/{lessonId}")
    @ResponseBody
    public Lesson getLesson(@PathVariable long lessonId) {
        return lessonService.getLessonById(lessonId);
    }

    @GetMapping("/{lessonId}/getSize")
    @ResponseBody
    public int getLessonSize(@PathVariable long lessonId) {
        return lessonService.getLessonSize(lessonId);
    }

    @PostMapping("/{lessonId}/{size}/setSize")
    @ResponseBody
    public void setLessonSize(@PathVariable long lessonId,
                              @PathVariable int size) {
        lessonService.setLessonSize(lessonId, size);
    }

    @GetMapping("/{lessonId}/getStartingTime")
    @ResponseBody
    public String getLessonStartingTime(@PathVariable long lessonId) {
        return lessonService.getLessonStartingTime(lessonId);
    }


    @PutMapping("/{title}/{startingTime}/{endingTime}/{size}/createNewLesson/admin")
    @ResponseBody
    public void createNewLesson(@PathVariable String title,
                                @PathVariable String startingTime,
                                @PathVariable String endingTime,
                                @PathVariable int size) {

        LocalDateTime startTime = LocalDateTime.parse(startingTime);
        LocalDateTime endTime = LocalDateTime.parse(endingTime);

        lessonService.addNewLesson(title, startTime, endTime, size);
    }
}