package ch.mywebsite.yannhoh.user;

import ch.mywebsite.yannhoh.exceptions.EmailAlreadyInUseException;
import ch.mywebsite.yannhoh.exceptions.UserDoesNotExistException;
import ch.mywebsite.yannhoh.exceptions.UsernameAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public void addUser(@RequestBody User user) throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        userService.addUser(user);
    }

    @DeleteMapping
    public void deleteUser(@RequestBody User user) throws UserDoesNotExistException {
        userService.deleteUser(user);
    }

    @PutMapping
    public void updateUser(@RequestBody User user)
            throws UserDoesNotExistException, UsernameAlreadyInUseException, EmailAlreadyInUseException {
        userService.updateUser(user);
    }
}
