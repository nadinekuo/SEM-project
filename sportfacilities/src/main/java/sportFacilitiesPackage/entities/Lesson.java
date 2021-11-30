package sportFacilitiesPackage.entities;

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

    /**
     * Empty constructor needed for Spring JPA.
     */
    public Lesson() {
    }

    /**
     * Constructor Lesson.
     *
     * @param lessonId        - long
     * @param title           - String
     */
    public Lesson(long lessonId, String title) {
        this.lessonId = lessonId;
        this.title = title;
        this.size = 0;
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
