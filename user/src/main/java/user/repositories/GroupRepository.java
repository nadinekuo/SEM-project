package user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.entities.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findByGroupId(long groupId);

}
