package mk.napijse.service.impl;

import mk.napijse.model.User;
import mk.napijse.model.enumerations.Role;
import mk.napijse.model.exceptions.InvalidUsernameOrPasswordException;
import mk.napijse.model.exceptions.PasswordsDoNotMatchException;
import mk.napijse.model.exceptions.UsernameAlreadyExistsException;
import mk.napijse.repository.UserRepository;
import mk.napijse.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByUsername(s).orElseThrow(()->new UsernameNotFoundException(s));
    }


    @Override
    public User register(String username, String password, String repeatPassword, String name, String surname, Role role) {
        String encrypted = this.passwordEncoder.encode(password);
        if (username==null || username.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);

        User user = new User(username,encrypted,name,username,role);

        return this.userRepository.save(user);
    }


}
