package user.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import user.entities.Customer;
import user.entities.Group;
import user.services.GroupService;

/**
 * The type Group controller.
 */
@RestController
@RequestMapping("group")
public class GroupController {

    @Autowired
    private final transient GroupService groupService;

    @Autowired
    private final transient RestTemplate restTemplate;

    private final transient String reservationUrl = "http://eureka-reservation";

    /**
     * Instantiates a new Group controller.
     *
     * @param groupService the group service
     */
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
        this.restTemplate = groupService.restTemplate();
    }

    /**
     * Gets group size.
     *
     * @param groupId the group id
     * @return the group size
     */
    @GetMapping("/{groupId}/getGroupSize")
    @ResponseBody
    public ResponseEntity<String> getGroupSize(@PathVariable Long groupId) {
        try {
            Integer groupSize = groupService.getGroupSizeById(groupId);
            return new ResponseEntity<String>(groupSize.toString(), HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("Group with id " + groupId + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets group by id.
     *
     * @param id the id
     * @return the group by id
     */
    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable long id) {
        return groupService.getGroupById(id);
    }

    /**
     * Gets users in a group.
     *
     * @param id the id
     * @return the users in a group
     */
    @GetMapping("/getCustomers/{id}")
    public List<Customer> getUsersInaGroup(@PathVariable long id) {
        return groupService.getUsersInaGroup(id);
    }

    /**
     * Create group response entity.
     *
     * @param groupName the group name
     * @return the response entity
     */
    @PostMapping("/create/{groupName}")
    public ResponseEntity<Group> createGroup(@PathVariable String groupName) {
        return new ResponseEntity<>(groupService.createGroup(groupName), HttpStatus.CREATED);
    }

    /**
     * Add customer to group response entity.
     *
     * @param customerId the customer id
     * @param groupId    the group id
     * @return the response entity
     */
    @PutMapping("/addCustomer/{groupId}/{customerId}")
    public ResponseEntity<Group> addCustomerToGroup(@PathVariable long customerId,
                                                    @PathVariable long groupId) {
        Group g = groupService.addCustomerToGroup(customerId, groupId);
        return ResponseEntity.ok(g);
    }

    // return the list of customers in the group

    /**
     * Make group reservation response entity.
     *
     * @param groupId     the group id
     * @param sportRoomId the sport room id
     * @param date        the date
     * @return the response entity
     */
    @PostMapping("/reservation/{groupId}/{sportRoomId}/{date}/makeSportRoomBooking")
    public ResponseEntity<String> makeGroupReservation(@PathVariable long groupId,
                                                       @PathVariable long sportRoomId,
                                                       @PathVariable String date) {

        String methodSpecificUrl = "/reservation/";

        List<Customer> customers;
        customers = groupService.getUsersInaGroup(groupId);

        for (Customer customer : customers) {

            String url = reservationUrl + methodSpecificUrl + customer.getId() + "/" + groupId + "/"
                + sportRoomId + "/" + date + "/" + "makeSportRoomBooking";

            System.out.println("customer Id : " + customer.getId());
            System.out.println(url);

            //call the makeSportRoomReservation API
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(customer), String.class);

        }
        return new ResponseEntity<>("Group Reservation Successful", HttpStatus.OK);
    }

}
