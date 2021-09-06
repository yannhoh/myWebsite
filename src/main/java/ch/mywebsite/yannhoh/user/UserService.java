package ch.mywebsite.yannhoh.user;

import ch.mywebsite.yannhoh.Role;
import ch.mywebsite.yannhoh.exceptions.EmailAlreadyInUseException;
import ch.mywebsite.yannhoh.exceptions.UserDoesNotExistException;
import ch.mywebsite.yannhoh.exceptions.UsernameAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;

//TODO API-Documentation with Open-API und dem yaml-file

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
        if (userByEmail.isPresent()) {
            throw new EmailAlreadyInUseException();
        } else if (userByName.isPresent()) {
            throw new UsernameAlreadyInUseException();
        } else {
            User completeUser = new User(user.getUsername(), user.getPassword(), user.getEmail());
            userRepository.save(completeUser);
        }
    }

    @PostMapping
    public void deleteUser(User user) throws UserDoesNotExistException {
        Long id = user.getId();
        Optional<User> userById = userRepository.findUserById(id);
        if (userById.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserDoesNotExistException();
        }

    }

    @PutMapping
    @Transactional
    public void updateUser(User updatedUser)
            throws UserDoesNotExistException, UsernameAlreadyInUseException, EmailAlreadyInUseException {
        User userById = userRepository.findUserById(updatedUser.getId()).orElseThrow(UserDoesNotExistException::new);
        String newUsername = updatedUser.getUsername();
        String newPassword = updatedUser.getPassword();
        String newEmail = updatedUser.getEmail();
        Role newRole = updatedUser.getRole();
        int counter = 0;

        if (newUsername != null && !newUsername.equals(userById.getUsername())) {
            if (userRepository.findUserByUsername(newUsername).isEmpty()) {
                userById.setUsername(newUsername);
                counter++;
            } else {
                throw new UsernameAlreadyInUseException();
            }
        }
        if (newEmail != null && !newEmail.equals(userById.getEmail())) {
            if (userRepository.findUserByEmail(newEmail).isEmpty()) {
                userById.setEmail(newEmail);
                counter++;
            } else {
                throw new EmailAlreadyInUseException();
            }
            userById.setEmail(newEmail);
            counter++;
        }
        if (newPassword != null && !newPassword.equals(userById.getPassword())) {
            userById.setPassword(newPassword);
            counter++;
        }
        if (newRole != null && newRole != userById.getRole()) {
            userById.setRole(newRole);
            counter++;
        }
        if (counter < 1) {
            throw new IllegalArgumentException("Nothing to update");
        }
    }
}
