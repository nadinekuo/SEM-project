package nl.tudelft.sem.template.controllers;

import nl.tudelft.sem.template.entities.User;
import nl.tudelft.sem.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Transient;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    /**
     * Autowired constructor for the class.
     * @param userService userService
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET mapping.
     * @param userId the id of the required student
     * @return a student with a specific id
     */
    @GetMapping("/{userId}")
    @ResponseBody
    public User getUser(@PathVariable long userId) {

        return userService.getUserById(userId);
    }

    // Examples of other API requests (POST, PUT, DELETE, GET)

//    /**
//     * POST mapping, adds a new student to a room.
//     * @param data the JSON of a Student object to be added to the DB
//     * @return id of the new student
//     */
//    @PostMapping("/addUser/Student")
//    @ResponseBody
//    public Long addStudent(@RequestBody String data) {
//        return userService.addUser(data);
//
//
//    /**
//     * DELETE mapping, removes a user from the DB.
//     * @param userId the ID of the user
//     * @return true if the user has been removed successfully and false otherwise
//     */
//    @DeleteMapping("/removeUser/{userId}")
//    @ResponseBody
//    public boolean removeUser(@PathVariable long userId) {
//        return userService.removeUser(userId);
//    }


    }