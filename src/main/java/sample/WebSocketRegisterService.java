package sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import sample.entity.Role;
import sample.entity.User;
import sample.repository.UserRepository;

import java.util.Collections;

@Component
public class WebSocketRegisterService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public String registration(final String username, final String password) {
        if (userRepository.findByUsername(username) != null) {
            return "Already registered";
        }
        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setUsername(username);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userRepository.save(user);

        return "Registered successfully";
    }
}
