package mk.napijse.service.impl;

import mk.napijse.model.User;
import mk.napijse.model.enumerations.Role;
import mk.napijse.repository.UserRepository;
import mk.napijse.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(String username, String password, String repeatPassword, String name, String surname, Role role) {
        return null;
    }
}
