package user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import user.entities.Customer;
import user.entities.Group;
import user.services.GroupService;

import java.util.List;

@RestController
@RequestMapping("group")
public class GroupController {

    @Autowired
    private final transient GroupService groupService;

    @Autowired
    private final transient RestTemplate restTemplate;

    private final String reservationURL = "http://eureka-reservation";

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
        this.restTemplate = groupService.restTemplate();
    }

    /**
     * @param groupId - Long
     * @return size of group, i.e. how many members it contains
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

    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable long id) {
        return groupService.getGroupById(id);
    }

    @GetMapping("/getCustomers/{id}")
    public List<Customer> getUsersInAGroup(@PathVariable long id) {
        return groupService.getUsersInAGroup(id);
    }

    @PostMapping("/create/{groupName}")
    public ResponseEntity<Group> createGroup(@PathVariable String groupName) {
        return new ResponseEntity<>(groupService.createGroup(groupName), HttpStatus.CREATED);
    }

    @PutMapping("/addCustomer/{groupId}/{customerId}")
    public ResponseEntity<Group> addCustomerToGroup(@PathVariable long customerId,
                                                    @PathVariable long groupId) {
        Group g = groupService.addCustomerToGroup(customerId, groupId);
        return ResponseEntity.ok(g);
    }

    // return the list of customers in the group

    @PostMapping("/reservation/{groupId}/{sportRoomId}/{date}/makeSportRoomBooking")
    public ResponseEntity<String> makeGroupReservation(@PathVariable long groupId,
                                                       @PathVariable long sportRoomId,
                                                       @PathVariable String date) {

        String methodSpecificUrl = "/reservation/";

        List<Customer> customers;
        customers = groupService.getUsersInAGroup(groupId);

        for (Customer customer : customers) {

            String url = reservationURL + methodSpecificUrl + customer.getId() + "/" + groupId + "/"
                + sportRoomId + "/" + date + "/" + "makeSportRoomBooking";

            System.out.println("customer Id : " + customer.getId());
            System.out.println(url);

            //call the makeSportRoomReservation API
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(customer), String.class);

        }
        return new ResponseEntity<>("Group Reservation Successful", HttpStatus.OK);
    }

}
