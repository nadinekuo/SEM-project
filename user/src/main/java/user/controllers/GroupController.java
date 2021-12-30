package user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import user.entities.Customer;
import user.services.GroupService;

import java.util.List;
import java.util.NoSuchElementException;

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
    public ResponseEntity<?> getGroupById(@PathVariable long id) {
        try{
            groupService.getGroupById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets users in a group.
     *
     * @param id the id
     * @return the users in a group
     */
    @GetMapping("/getCustomers/{id}")
    public ResponseEntity<?> getUsersInaGroup(@PathVariable long id) {
        try{
            groupService.getUsersInaGroup(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * returns the group by group name.
     * @param groupName
     * @return the Group
     */
    @GetMapping("/groupName/{groupName}")
    public ResponseEntity<?> getGroupByGroupName(@PathVariable String groupName) {
        try{
            groupService.getGroupByGroupName(groupName);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(IllegalStateException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Create group response entity.
     *
     * @param groupName the group name
     * @return the response entity
     */
    @PostMapping("/create/{groupName}")
    public ResponseEntity<?> createGroup(@PathVariable String groupName) {
        try{
            groupService.createGroup(groupName);
            return new ResponseEntity<>("Group created successfully", HttpStatus.OK);
        }catch(IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Add customer to group response entity.
     *
     * @param customerId the customer id
     * @param groupId    the group id
     * @return the response entity
     */
    @PutMapping("/addCustomer/{groupId}/{customerId}")
    public ResponseEntity<?> addCustomerToGroup(@PathVariable long customerId,
                                                    @PathVariable long groupId) {
        try{
            groupService.addCustomerToGroup(customerId, groupId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Make group reservation response entity.
     *
     * @param groupId     the group id
     * @param sportRoomId the sport room id
     * @param date        the date
     * @return the response entity
     */
    @PostMapping("/reservation/{groupId}/{sportRoomId}/{date}/makeSportRoomBooking")
    public ResponseEntity<?> makeGroupReservation(@PathVariable long groupId,
                                                       @PathVariable long sportRoomId,
                                                       @PathVariable String date) {

        String methodSpecificUrl = "/reservation";

        List<Customer> customers;
        customers = groupService.getUsersInaGroup(groupId);

        for (Customer customer : customers) {

            String url = reservationUrl
                + methodSpecificUrl
                + "/" + customer.getId()
                + "/" + groupId
                + "/" + date
                + "/" + sportRoomId
                + "/" + customer.isPremiumUser()
                + "/" + "makeSportRoomBooking";

            System.out.println("customer Id : " + customer.getId());
            System.out.println(url);

            //call the makeSportRoomReservation API
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(customer), String.class);

        }
        return new ResponseEntity<>("Group Reservation Successful", HttpStatus.OK);
    }

}
