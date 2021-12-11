package sportfacilities.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The type Lesson.
 */
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
     * Instantiates a new Lesson.
     */
    public Lesson() {
    }

    /**
     * Instantiates a new Lesson.
     *
     * @param title        the title
     * @param startingTime the starting time
     * @param endingTime   the ending time
     * @param size         the size
     */
    public Lesson(String title, LocalDateTime startingTime, LocalDateTime endingTime, int size) {
        this.title = title;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.size = size;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets lesson id.
     *
     * @return the lesson id
     */
    public Long getLessonId() {
        return lessonId;
    }

    /**
     * Sets lesson id.
     *
     * @param lessonId the lesson id
     */
    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets starting time.
     *
     * @return the starting time
     */
    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    /**
     * Sets starting time.
     *
     * @param startingTime the starting time
     */
    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    /**
     * Gets ending time.
     *
     * @return the ending time
     */
    public LocalDateTime getEndingTime() {
        return endingTime;
    }

    /**
     * Sets ending time.
     *
     * @param endingTime the ending time
     */
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