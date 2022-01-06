package user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import user.entities.Group;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     *
     * @param groupId
     * @return
     */
    Optional<Group> findByGroupId(Long groupId);

    /**
     * find the Group by group name
     *
     * @param groupName
     * @return the Group
     */
    @Query(value = "SELECT * " + "FROM GROUPS " + "WHERE GROUP_NAME = ?1", nativeQuery = true)
    Optional<Group> findByGroupName(String groupName);

}
