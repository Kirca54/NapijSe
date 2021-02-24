package mk.napijse.service;

import mk.napijse.model.SecureToken;
import mk.napijse.model.User;
import mk.napijse.model.exceptions.InvalidTokenException;
import mk.napijse.model.exceptions.UserNotFoundException;

public interface MailVerificationService {
    SecureToken createSecureToken();
    void saveSecureToken(final SecureToken token);
    SecureToken findByToken(final String token);
    void removeToken(final SecureToken token);
    void removeTokenByToken(final String token);
}
