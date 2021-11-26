package repositories;

import entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Lesson findById(long lessonId);

    @Transactional
    void deleteById(Long lessonId);
}
