package nl.tudelft.sem.sportfacilities.repositories;

import nl.tudelft.sem.sportfacilities.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Transactional
    void deleteById(Long lessonId);
}
