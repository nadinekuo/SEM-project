package user.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import user.entities.Customer;
import user.entities.Group;
import user.repositories.GroupRepository;

/**
 * The type Group service.
 */
@Service
public class GroupService {

    @Autowired
    private final GroupRepository groupRepository;
    @Autowired
    private final CustomerService customerService;

    public GroupService(GroupRepository groupRepository, CustomerService customerService) {
        this.groupRepository = groupRepository;
        this.customerService = customerService;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Creates a group by saving it in the Group database.
     *
     * @param groupName - name of group
     * @return true if group created, else false
     */
    public boolean createGroup(String groupName) {
        boolean res = false;
        if (groupRepository.findByGroupName(groupName).isPresent()) {
            throw new IllegalStateException(
                "group with name " + groupName + " already exists! Try a new name");
        } else {
            groupRepository.save(new Group(groupName, new ArrayList<>()));
            res = true;
        }
        return res;
    }

    /**
     * Finds Group by id and returns size.
     *
     * @param groupId - long
     * @return group size
     */
    public int getGroupSizeById(Long groupId) {
        Group group = groupRepository.findByGroupId(groupId).orElseThrow(
            () -> new IllegalStateException("Group with id " + groupId + " does not exist!"));
        return group.getGroupSize();
    }

    /**
     * Finds Group by id.
     *
     * @param groupId - long
     * @return Optional of Group having this id
     */
    public Group getGroupById(Long groupId) {
        return groupRepository.findByGroupId(groupId).orElseThrow(
            () -> new IllegalStateException("Group with id " + groupId + " does not exist!"));

    }

    public Group getGroupByGroupName(String groupName) {
        return groupRepository.findByGroupName(groupName).orElseThrow(
            () -> new IllegalStateException("Group with name " + groupName + " does not exist!"));
    }

    /**
     * Add customer to group group.
     *
     * @param customerId the customer id
     * @param groupId    the group id
     * @return the group
     */
    public Group addCustomerToGroup(long customerId, long groupId) {
        Customer oldCustomer = customerService.getCustomerById(customerId);

        Group groupToAdd = groupRepository.findByGroupId(groupId).orElseThrow(
            () -> new IllegalStateException("Group with id " + groupId + " does not exist!"));
        oldCustomer.addGroupToUsersGroupList(groupToAdd);
        //customer service only called for the persistence of the updated group attribute of the
        // customer
        customerService.saveCustomer(oldCustomer);
        return groupToAdd;

    }

    public List<Customer> getUsersInAGroup(long groupId) {
        Group group = groupRepository.findByGroupId(groupId).orElseThrow(
            () -> new IllegalStateException("Group with id " + groupId + " does not exist!"));
        return group.getGroupMembers();
    }

}
