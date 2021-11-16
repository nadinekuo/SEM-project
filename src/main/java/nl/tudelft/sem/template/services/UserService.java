package nl.tudelft.sem.template.services;

import nl.tudelft.sem.template.entities.User;
import nl.tudelft.sem.template.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    /** Constructor for UserService.
     * @param userRepository - retrieves Users from database.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Finds User by id.
     * @param userId - long
     * @return Optional of User having this id
     */
    public User getUserById(long userId) {
        return userRepository.findById(userId);
    }




}
