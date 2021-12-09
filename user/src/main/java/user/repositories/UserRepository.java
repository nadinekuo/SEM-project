package user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import user.entities.User;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

    T findById(long userId);

    @Transactional
    void deleteById(Long userId);

}
