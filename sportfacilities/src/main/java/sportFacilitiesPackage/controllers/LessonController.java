package sportFacilitiesPackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sportFacilitiesPackage.entities.Lesson;
import sportFacilitiesPackage.services.LessonService;

@RestController
@RequestMapping("lessons")
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
     * @param lessonId the id of the required student
     * @return a student with a specific id
     */
    @GetMapping("/{lessonId}")
    @ResponseBody
    public Lesson getLesson(@PathVariable long lessonId) {

        return lessonService.getLessonById(lessonId);
    }


}
