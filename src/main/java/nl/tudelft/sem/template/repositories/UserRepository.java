package nl.tudelft.sem.template.repositories;

import nl.tudelft.sem.template.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

    T findById(long userId);

    @Transactional
    void deleteById(Long userId);
}
