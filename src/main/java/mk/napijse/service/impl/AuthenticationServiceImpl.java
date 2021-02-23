package mk.napijse.service.impl;


import mk.napijse.model.User;
import mk.napijse.model.exceptions.InvalidArgumentsException;
import mk.napijse.model.exceptions.InvalidUserCredentialsException;
import mk.napijse.repository.UserRepository;
import mk.napijse.service.AuthenticationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User login(String username, String password) {
        if (username==null || username.isEmpty() || password==null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        String encrypted = this.encoder.encode(password);
        return userRepository.findByUsernameAndPassword(username,
                encrypted).orElseThrow(InvalidUserCredentialsException::new);
    }

}
