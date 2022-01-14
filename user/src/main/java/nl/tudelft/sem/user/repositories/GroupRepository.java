package nl.tudelft.sem.user.repositories;

import java.util.Optional;
import nl.tudelft.sem.user.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * Find group by group id.
     *
     * @param groupId group id
     * @return Optional of group
     */
    Optional<Group> findByGroupId(Long groupId);

    /**
     * Find the Group by group name.
     *
     * @param groupName group name
     * @return optional of group
     */
    @Query(value = "SELECT * " + "FROM GROUPS " + "WHERE GROUP_NAME = ?1", nativeQuery = true)
    Optional<Group> findByGroupName(String groupName);

}
