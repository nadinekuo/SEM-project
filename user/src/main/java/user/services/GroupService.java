package user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.entities.Customer;
import user.entities.Group;
import user.repositories.GroupRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

//    private final transient GroupRepository groupRepository;
//
//    /**
//     * Constructor for GroupService.
//     *
//     * @param groupRepository - retrieves Groups from database.
//     */
//    @Autowired
//    public GroupService(GroupRepository groupRepository) {
//        this.groupRepository = groupRepository;
//    }
//
//
//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//
//
//    /**
//     * Finds Group by id.
//     *
//     * @param groupId - long
//     * @return Optional of Group having this id
//     */
//    public Group getGroupById(Long groupId) {
//        return groupRepository.findByGroupId(groupId)
//            .orElseThrow(() ->
//                new IllegalStateException("Group with id "
//                    + groupId + " does not exist!"));
//    }
//
//
//    /**
//     * Finds Group by id and returns size.
//     *
//     * @param groupId - long
//     * @return group size
//     */
//    public int getGroupSizeById(Long groupId) {
//        Group group = groupRepository.findByGroupId(groupId)
//            .orElseThrow(() ->
//                new IllegalStateException("Group with id "
//                    + groupId + " does not exist!"));
//        return group.getGroupSize();
//    }

    @Autowired
    private final GroupRepository groupRepository;

    @Autowired
    private final CustomerService customerService;


    public GroupService(GroupRepository groupRepository, CustomerService customerService) {
        this.groupRepository = groupRepository;
        this.customerService = customerService;
    }

    public Group getGroupById(long id) {
        return groupRepository.findById(id).orElse(null);
    }


    public Group createGroup(String groupName) {
        return groupRepository.save(new Group(groupName, new ArrayList<>()));
    }

    public Group addCustomerToGroup(long customerId, long groupId) {
        Customer oldCustomer = customerService.getCustomerById(customerId);
        Group groupToAdd = groupRepository.findByGroupId(groupId);
        oldCustomer.addGroupToUsersGroupList(groupToAdd);
        //customer service only called for persistence
        customerService.saveCustomer(oldCustomer);
        return groupToAdd;

    }

    public List<Customer> getUsersInAGroup(long groupId) {
        Group group = groupRepository.findByGroupId(groupId);
        return group.getGroupMembers();
    }


}
