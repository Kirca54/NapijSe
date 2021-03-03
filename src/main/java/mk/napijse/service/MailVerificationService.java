package mk.napijse.service;

import mk.napijse.model.entities.SecureToken;

public interface MailVerificationService {
    SecureToken createSecureToken();
    void saveSecureToken(final SecureToken token);
    SecureToken findByToken(final String token);
    void removeToken(final SecureToken token);
    void removeTokenByToken(final String token);
}
