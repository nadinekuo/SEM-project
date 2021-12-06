package sportFacilitiesPackage.repositories;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sportFacilitiesPackage.entities.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    //Optional<Lesson> findById(Long lessonId);

    @Transactional
    void deleteById(Long lessonId);
}
