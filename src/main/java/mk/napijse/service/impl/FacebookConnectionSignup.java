package mk.napijse.service.impl;

import mk.napijse.model.entities.User;
import mk.napijse.model.enumerations.Role;
import mk.napijse.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public FacebookConnectionSignup(UserRepository userRepository,
                                    PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public String execute(Connection<?> connection) {
        User user = new User();
        user.setUsername(connection.getDisplayName());
        user.setPassword(this.encoder.encode("facebookpassword"));
        user.setAccountVerified(true);
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        return user.getUsername();
    }
}
