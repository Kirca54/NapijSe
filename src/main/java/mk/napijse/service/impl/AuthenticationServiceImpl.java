package mk.napijse.service.impl;


import mk.napijse.model.User;
import mk.napijse.model.exceptions.InvalidArgumentsException;
import mk.napijse.model.exceptions.InvalidUserCredentialsException;
import mk.napijse.repository.UserRepository;
import mk.napijse.service.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username==null || username.isEmpty() || password==null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByUsernameAndPassword(username,
                password).orElseThrow(InvalidUserCredentialsException::new);
    }

}
