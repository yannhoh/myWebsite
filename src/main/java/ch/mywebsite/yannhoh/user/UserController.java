package ch.mywebsite.yannhoh.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "user")
public class UserController {

    private final UserService userService;

    //userService will be instantiated automatically with @Autowired notation
    @Autowired
    public UserController (UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
}

    @PostMapping
    public void addUser(@RequestBody User user){
        userService.addUser(user);
    }
}
