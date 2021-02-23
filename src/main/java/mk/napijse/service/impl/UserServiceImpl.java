package mk.napijse.service.impl;

import mk.napijse.model.SecureToken;
import mk.napijse.model.User;
import mk.napijse.model.context.AccountVerificationEmailContext;
import mk.napijse.model.enumerations.Role;
import mk.napijse.model.exceptions.*;
import mk.napijse.repository.SecureTokenRepository;
import mk.napijse.repository.UserRepository;
import mk.napijse.service.MailService;
import mk.napijse.service.MailVerificationService;
import mk.napijse.service.UserService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Objects;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailVerificationService mailVerificationService;

    @Autowired
    SecureTokenRepository secureTokenRepository;





    @Value("${site.base.url.https}")
    private String baseURL;






    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User customer = userRepository.findByEmail(email);
        if (customer == null) {
            throw new UsernameNotFoundException(email);
        }
        if(!customer.isAccountVerified()){
            throw new RuntimeException("User is not verified");
        }
        return customer;
    }


    @Override
    public User register(String username, String password, String repeatPassword, String email, String name, String surname, Role role) {
        String encrypted = this.passwordEncoder.encode(password);
        if (username==null || username.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);

        User user = new User(name, surname, email, username, encrypted, role);
        user = this.userRepository.save(user);
        sendRegistrationConfirmationEmail(user);
        return user;
    }

    @Override
    public boolean checkIfUserExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public void sendRegistrationConfirmationEmail(User user) {
        SecureToken secureToken= mailVerificationService.createSecureToken();
        secureToken.setUser(user);
        secureTokenRepository.save(secureToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            mailService.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean verifyUser(String token) throws InvalidTokenException {
        SecureToken secureToken = secureTokenRepository.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("Token is not valid");
        }
        User user = userRepository.getOne(secureToken.getUser().getUsername());

        user.setAccountVerified(true);
        userRepository.save(user); // let's same user details

        // we don't need invalid password now
        secureTokenRepository.removeByToken(token);
        return true;
    }

    @Override
    public User getUserById(String id) throws UserNotFoundException {
        User user= userRepository.findByEmail(id);
        if(user == null || BooleanUtils.isFalse(user.isAccountVerified())){
            // we will ignore in case account is not verified or account does not exists
            throw new UserNotFoundException("unable to find account or account is not active");
        }
        return user;
    }


}
