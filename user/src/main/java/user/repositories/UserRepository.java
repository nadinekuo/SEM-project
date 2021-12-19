package user.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import user.entities.Admin;
import user.entities.Customer;
import user.entities.User;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

    T findById(long userId);

    @Transactional
    void deleteById(Long userId);

    Optional<T> findByUsername(String username);

    @Query(value = "SELECT * " + "FROM customers "
            + "WHERE username = ?1", nativeQuery = true)
    Optional<Customer> findCustomerByUsername(String username);

    @Query(value = "SELECT * " + "FROM admins "
            + "WHERE username = ?1", nativeQuery = true)
    Optional<Admin> findAdminByUsername(String username);
}
