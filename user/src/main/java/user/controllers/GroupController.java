package user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import user.entities.Customer;
import user.services.GroupService;
import user.services.UserService;

@RestController
@RequestMapping("group")
public class GroupController {

    private final transient GroupService groupService;

    @Autowired
    private transient final RestTemplate restTemplate;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
        this.restTemplate = groupService.restTemplate();
    }


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


}
