package user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import user.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

    /**
     * find user by Id, change
     *
     * @param userId
     * @return Optional
     */
    Optional<T> findById(long userId);

    /**
     * delete the user by Id.
     *
     * @param userId
     */
    @Transactional
    void deleteById(Long userId);

    /**
     * find user by username.
     *
     * @param username
     * @return
     */
    Optional<T> findByUsername(String username);
}
