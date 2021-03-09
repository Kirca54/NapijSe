package mk.napijse.service.impl;


import mk.napijse.model.context.ForgotPasswordEmailContext;
import mk.napijse.model.entities.SecureToken;
import mk.napijse.model.entities.User;
import mk.napijse.model.exceptions.*;
import mk.napijse.repository.SecureTokenRepository;
import mk.napijse.repository.UserRepository;
import mk.napijse.service.AuthenticationService;
import mk.napijse.service.MailService;
import mk.napijse.service.SecureTokenService;
import mk.napijse.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("http://localhost:8080")
    private String baseURL;

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final SecureTokenRepository secureTokenRepository;
    private final SecureTokenService tokenService;
    private final MailService mailService;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     PasswordEncoder encoder,
                                     UserService userService,
                                     SecureTokenRepository secureTokenRepository,
                                     SecureTokenService tokenService,
                                     MailService mailService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userService = userService;
        this.secureTokenRepository = secureTokenRepository;
        this.tokenService = tokenService;
        this.mailService = mailService;
    }

    @Override
    public User login(String username, String password) {
        if (username==null || username.isEmpty() || password==null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (this.encoder.matches(password, user.getPassword()))
            return user;
        else throw new InvalidUserCredentialsException();
    }

    @Override
    public void forgotPassword(String username) throws UserNotFoundException {
        User user = this.userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        this.sendResetPasswordEmail(user);
    }

    @Override
    public void updatePassword(String password,
                               String repeatedPassword,
                               String token) throws PasswordsDoNotMatchException, InvalidTokenException, UserNotFoundException {
        if (!password.equals(repeatedPassword))
            throw new PasswordsDoNotMatchException();
        SecureToken secureToken = this.tokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("Token is not valid");
        }
        User user = this.secureTokenRepository.findByToken(token).getUser();
        if(Objects.isNull(user)){
            throw new UserNotFoundException("Unable to find user for the token");
        }
        this.tokenService.removeToken(secureToken);
        user.setPassword(this.encoder.encode(password));
        this.userRepository.save(user);
    }

    @Override
    public boolean loginDisabled(String email) {
        User user = this.userRepository.findByEmail(email);
        return user != null && user.isLoginDisabled();
    }

    protected void sendResetPasswordEmail(User user) {
        SecureToken secureToken = tokenService.createSecureToken();
        secureToken.setUser(user);
        secureTokenRepository.save(secureToken);
        ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(this.baseURL, secureToken.getToken());
        try {
            this.mailService.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
