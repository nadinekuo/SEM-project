package user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import user.entities.Admin;
import user.entities.Group;
import user.entities.User;
import user.repositories.GroupRepository;
import user.repositories.UserRepository;

@Service
public class GroupService {

    private final transient GroupRepository groupRepository;

    /**
     * Constructor for GroupService.
     *
     * @param groupRepository - retrieves Groups from database.
     */
    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /**
     * Finds Group by id.
     *
     * @param groupId - long
     * @return Optional of Group having this id
     */
    public Group getGroupById(Long groupId) {
        return groupRepository.findByGroupId(groupId)
            .orElseThrow(() ->
                new IllegalStateException("Group with id "
                    + groupId + " does not exist!"));
    }


    /**
     * Finds Group by id and returns size.
     *
     * @param groupId - long
     * @return group size
     */
    public int getGroupSizeById(Long groupId) {
        Group group = groupRepository.findByGroupId(groupId)
            .orElseThrow(() ->
                new IllegalStateException("Group with id "
                    + groupId + " does not exist!"));
        return group.getGroupMembers().size();
    }



}
