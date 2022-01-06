package user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import user.entities.Admin;
import user.entities.Customer;
import user.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

//    /**
//     * See if there is a user matching the given userId.
//     *
//     * @param userId the userId
//     * @return the optional user
//     */
//    T findById(long userId);

    /**
     * find user by Id
     *
     * @param userId
     * @return Optional
     */
    Optional<T> findById(long userId);

    /**
     * Delete a user matching the given userId.
     *
     * @param userId the userId
     */
    @Transactional
    void deleteById(Long userId);

    /**
     * See if there is a customer matching the given username.
     *
     * @param username the username
     * @return the optional customer
     */
    @Query(value = "SELECT * " + "FROM customers " + "WHERE username = ?1", nativeQuery = true)
    Optional<Customer> findCustomerByUsername(String username);

    /**
     * See if there is an admin matching the given username.
     *
     * @param username the username
     * @return the optional admin
     */
    @Query(value = "SELECT * " + "FROM admins " + "WHERE username = ?1", nativeQuery = true)
    Optional<Admin> findAdminByUsername(String username);

    /**
     * find user by username.
     *
     * @param username
     * @return
     */
    Optional<T> findByUsername(String username);
}
