package nl.tudelft.sem.sportfacilities.entities;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class LessonTest {

    private final transient Lesson tango;
    private final transient Lesson hiit;

    public LessonTest() {
        LocalDateTime startingDate = LocalDateTime.of(2021, 1, 1, 1, 1);
        LocalDateTime endingDate = LocalDateTime.of(2021, 1, 1, 1, 1);

        tango = new Lesson("Tango", startingDate, endingDate, 10);
        hiit = new Lesson("HIIT", startingDate, endingDate, 10);
    }

    @Test
    void setSizeTest() {
        tango.setSize(15);
        assertThat(tango.getSize()).isEqualTo(15);
    }

    @Test
    void setLessonIdTest() {
        tango.setLessonId(42L);
        assertThat(tango.getLessonId()).isEqualTo(42L);
    }

    @Test
    void setTitleTest() {
        tango.setTitle("tango1");
        assertThat(tango.getTitle()).isEqualTo("tango1");
    }

    @Test
    void setStartingTimeTest() {
        LocalDateTime startingDate = LocalDateTime.of(2021, 2, 2, 2, 2);
        tango.setStartingTime(startingDate);
        assertThat(tango.getStartingTime()).isEqualTo(startingDate);
    }

    @Test
    void setEndingTimeTest() {
        LocalDateTime endingDate = LocalDateTime.of(2021, 2, 2, 2, 2);
        tango.setEndingTime(endingDate);
        assertThat(tango.getEndingTime()).isEqualTo(endingDate);
    }

    @Test
    void equalsTest() {
        tango.setLessonId(20L);
        hiit.setLessonId(15L);
        assertThat(tango.equals(hiit)).isFalse();

        Lesson tango2 = tango;
        assertTrue(tango.equals(tango2));
    }

    @Test
    void toStringTest() {
        tango.setLessonId(20L);
        String res = "Lesson{lessonId=20, title='Tango'}";
        assertThat(tango.toString()).isEqualTo(res);
    }

    @Test
    void notEqualsTest() {
        Lesson boxing = null;
        assertFalse(tango.equals(boxing));
    }
}