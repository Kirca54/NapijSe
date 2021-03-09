package mk.napijse.config;

import mk.napijse.model.entities.User;
import mk.napijse.service.AuthenticationService;
import mk.napijse.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public CustomUsernamePasswordAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        System.out.println("authenticate "+username);
        System.out.println("authenticate "+password);
        System.out.println(this.passwordEncoder.encode(password));

        if ("".equals(username) || "".equals(password)) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        User user = this.authenticationService.login(username, password);

        if (!this.passwordEncoder.matches(password, user.getPassword())) {
        //if (!password.equals(user.getPassword())){
            throw new BadCredentialsException("Password is incorrect!");
        }
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

    }


    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
