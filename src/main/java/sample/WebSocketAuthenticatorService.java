package sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import sample.entity.User;
import sample.repository.UserRepository;

import java.util.Collections;

@Component
public class WebSocketAuthenticatorService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    // This method MUST return a UsernamePasswordAuthenticationToken instance, the spring security chain is testing it with 'instanceof' later on. So don't use a subclass of it or any other class
    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String username, final String password) throws AuthenticationException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }
        // Add your own logic for retrieving user in fetchUserFromDb()
//        if (fetchUserFromDb(username, password) == null) {
//            throw new BadCredentialsException("Bad credentials for user " + username);
//        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("Bad credentials for user " + username);
        }

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Bad password for user " + username);
        }
        // null credentials, we do not pass the password along
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
        );
    }
}
