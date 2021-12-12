package user.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.entities.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByGroupId(Long groupId);

}
