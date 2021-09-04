package ch.mywebsite.yannhoh.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public void addUser(User user) {
        Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());
        Optional<User> userByName = userRepository.findUserByUsername(user.getUsername());
        if(userByEmail.isPresent()) {
            throw new IllegalStateException("E-mail address is already in use");
            //TODO in Antwort den Fehlergrund angeben
        } else if(userByName.isPresent()) {
            throw new IllegalStateException("Username is already in use");
            //TODO in Antwort den Fehlergrund angeben
        } else {
            User completeUser = new User(user.getUsername(), user.getPassword(), user.getEmail());
            userRepository.save(completeUser);
        }
    }
}
