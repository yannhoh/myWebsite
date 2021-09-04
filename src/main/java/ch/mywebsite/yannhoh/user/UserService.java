package ch.mywebsite.yannhoh.user;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class UserService {

    @PostMapping
    public void addUser(String username, String password, String email) {

    }


    @GetMapping
    public List<User> getAllUsers() {
        return List.of(
                new User("y4nnkee", "12", "sd@swd.ch"),
                new User("meli", "34", "dfg@kl.sd")
        );
    }

}
