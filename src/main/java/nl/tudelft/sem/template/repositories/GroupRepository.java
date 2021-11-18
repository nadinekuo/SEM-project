package nl.tudelft.sem.template.repositories;

import nl.tudelft.sem.template.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findByGroupId(long groupId);


}
