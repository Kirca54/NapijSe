package mk.napijse.service.impl;


import mk.napijse.model.entities.User;
import mk.napijse.model.exceptions.InvalidArgumentsException;
import mk.napijse.model.exceptions.InvalidUserCredentialsException;
import mk.napijse.model.exceptions.UserNotFoundException;
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
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (this.encoder.matches(password, user.getPassword()))
            return user;
        else throw new InvalidUserCredentialsException();
        //return userRepository.findByUsernameAndPassword(username,
        //        password).orElseThrow(InvalidUserCredentialsException::new);
    }

}
