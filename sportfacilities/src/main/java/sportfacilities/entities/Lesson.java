package sportfacilities.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @SequenceGenerator(name = "lesson_sequence", sequenceName = "lesson_sequence",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_sequence")
    private long lessonId;

    private String title;
    private int size;     // how many enrolled attendees
    private LocalDateTime startingTime;
    private LocalDateTime endingTime;

    /**
     * Empty constructor needed for Spring JPA.
     */
    public Lesson() {
    }

    /**
     * Constructor Lesson.
     * @param title    - String
     * @param startingTime
     * @param endingTime
     */
    public Lesson(String title, LocalDateTime startingTime, LocalDateTime endingTime, int size) {
        this.title = title;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalDateTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalDateTime endingTime) {
        this.endingTime = endingTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lesson lesson = (Lesson) o;
        return lessonId == lesson.lessonId;
    }

    @Override
    public String toString() {
        return "Lesson{" + "lessonId=" + lessonId + ", title='" + title;
    }
}