package user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("groups")
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
    public int getGroupSize(@PathVariable Long groupId) {
        return groupService.getGroupSizeById(groupId);
    }


}
