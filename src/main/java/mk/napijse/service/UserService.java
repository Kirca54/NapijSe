package mk.napijse.service;

import mk.napijse.model.User;
import mk.napijse.model.enumerations.Role;
import mk.napijse.model.exceptions.InvalidTokenException;
import mk.napijse.model.exceptions.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(String username, String password, String repeatPassword, String email, String name, String surname, Role role);
    boolean checkIfUserExist(final String email);
    void sendRegistrationConfirmationEmail(final User user);
    boolean verifyUser(final String token) throws InvalidTokenException;
    User getUserById(final String id) throws UserNotFoundException;
}
