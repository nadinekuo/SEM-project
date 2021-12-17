package user.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import user.entities.User;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

    T findById(long userId);

    T findUserByUsername(String username);

    @Transactional
    void deleteById(Long userId);

    Optional<T> findByUsername(String username);
}
