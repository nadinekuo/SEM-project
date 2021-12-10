package sportfacilities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sportfacilities.entities.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    //Lesson findLessonById(Long lessonId);

    @Transactional
    void deleteById(Long lessonId);
}
