package sportfacilities.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getLesson(@PathVariable long lessonId) {
        try {
            Lesson lesson = lessonService.getLessonById(lessonId);
            return new ResponseEntity<>(lesson, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets lesson size.
     *
     * @param lessonId the lesson id
     * @return the lesson size
     */
    @GetMapping("/{lessonId}/getSize")
    @ResponseBody
    public ResponseEntity<String> getLessonSize(@PathVariable long lessonId) {
        try {
            Integer lessonSize = lessonService.getLessonSize(lessonId);
            return new ResponseEntity<>(lessonSize.toString(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Sets lesson size.
     *
     * @param lessonId the lesson id
     * @param size     the size
     */
    @PostMapping("/{lessonId}/{size}/setSize/admin")
    @ResponseBody
    public ResponseEntity<String> setLessonSize(@PathVariable long lessonId,
                                               @PathVariable int size) {
        try {
            lessonService.setLessonSize(lessonId, size);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets lesson starting time.
     *
     * @param lessonId the lesson id
     * @return the lesson starting time
     */
    @GetMapping("/{lessonId}/getStartingTime")
    @ResponseBody
    public ResponseEntity<String> getLessonStartingTime(@PathVariable long lessonId) {
        try {
            String startingTime = lessonService.getLessonStartingTime(lessonId);
            return new ResponseEntity<>(startingTime, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<String> createNewLesson(@PathVariable String title,
                                             @PathVariable String startingTime,
                                             @PathVariable String endingTime,
                                             @PathVariable int size) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(startingTime);
            LocalDateTime endTime = LocalDateTime.parse(endingTime);
            lessonService.addNewLesson(title, startTime, endTime, size);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete lesson.
     *
     * @param lessonId the lesson id
     */
    @DeleteMapping("/{lessonId}/admin")
    @ResponseBody
    public ResponseEntity<String> deleteLesson(@PathVariable long lessonId) {
        try {
            lessonService.deleteLesson(lessonId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}