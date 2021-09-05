package ch.mywebsite.yannhoh.user;

import ch.mywebsite.yannhoh.exceptions.EmailAlreadyInUseException;
import ch.mywebsite.yannhoh.exceptions.UsernameAlreadyInUseException;
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
    public void addUser(User user) throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());
        Optional<User> userByName = userRepository.findUserByUsername(user.getUsername());
        if(userByEmail.isPresent()) {
            throw new EmailAlreadyInUseException();
        } else if(userByName.isPresent()) {
            throw new UsernameAlreadyInUseException();
        } else {
            User completeUser = new User(user.getUsername(), user.getPassword(), user.getEmail());
            userRepository.save(completeUser);
        }
    }

}
