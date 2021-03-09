package mk.napijse.service;

import mk.napijse.model.entities.User;
import mk.napijse.model.exceptions.InvalidTokenException;
import mk.napijse.model.exceptions.PasswordsDoNotMatchException;
import mk.napijse.model.exceptions.UserNotFoundException;

public interface AuthenticationService {

        User login(String username, String password);
        void forgotPassword(final String username) throws UserNotFoundException;
        void updatePassword(final String password, final String repeatedPassword, final String token) throws PasswordsDoNotMatchException, InvalidTokenException, UserNotFoundException;
        boolean loginDisabled(final String email);
}
