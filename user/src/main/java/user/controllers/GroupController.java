package user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import user.entities.Customer;
import user.entities.Group;
import user.services.GroupService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("group")
public class GroupController {

   

    @Autowired
    private final GroupService groupService;

    //private final transient GroupService groupService;
//    @Autowired
//    private transient final RestTemplate restTemplate;
//
//    public GroupController(GroupService groupService) {
//        this.groupService = groupService;
//        this.restTemplate = groupService.restTemplate();
//    }
//
//
//    @GetMapping("/{groupId}/getGroupSize")
//    @ResponseBody
//    public ResponseEntity<String> getGroupSize(@PathVariable Long groupId) {
//        try {
//            Integer groupSize = groupService.getGroupSizeById(groupId);
//            return new ResponseEntity<String>(groupSize.toString(), HttpStatus.OK);
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            System.out.println("Group with id " + groupId + " does not exist!!");
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//    }

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable long id){
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
    public ResponseEntity<Group> addCustomerToGroup(@PathVariable long customerId, @PathVariable long groupId){
        Group g = groupService.addCustomerToGroup(customerId, groupId);
        return ResponseEntity.ok(g);
    }


    // return the list of customers in the group


    @PutMapping("/reservation/{groupId}/{sportRoomId}/{date}/makeSportRoomBooking")
    public void makeGroupReservation(@PathVariable long groupId,
                                     @PathVariable long sportRoomId,
                                     @PathVariable String date){

        String reservationURL = "http://localhost:8086/reservation/";

        RestTemplate restTemplate = new RestTemplate();

        List<Customer> customers = new ArrayList<>();
        customers = groupService.getUsersInAGroup(groupId);

        for(Customer customer : customers){

            String url = "http://localhost:8086/reservation/" +
                    customer.getId() + "/" +
                    groupId + "/" +
                    sportRoomId + "/" +
                    date + "/" + "/makeSportRoomBooking";


            //call the makeSportRoomReservation API
            String result = restTemplate.getForObject(url, String.class);
        }
    }





}
