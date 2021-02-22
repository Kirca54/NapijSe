package mk.napijse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    public UserDetailsService userDetailsService;


    private final PasswordEncoder passwordEncoder;

    private final CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;

    public SecurityConfig(PasswordEncoder passwordEncoder,
                          CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider) {
        this.passwordEncoder = passwordEncoder;
        this.customUsernamePasswordAuthenticationProvider = customUsernamePasswordAuthenticationProvider;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // TODO: If you are implementing the security requirements, remove this following line
        // web.ignoring().antMatchers("/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //http.csrf().disable()
        //        .authorizeRequests()
        //        .antMatchers("/").permitAll()
        //        .anyRequest().hasRole("ADMIN")
        //        .and()
        //        .formLogin()
        //        .failureUrl("/login?error=BadCredentials")
        //        .defaultSuccessUrl("/home", true)
        //        .and()
        //        .logout()
        //        .logoutUrl("/logout")
        //        .clearAuthentication(true)
        //        .invalidateHttpSession(true)
        //        .deleteCookies("JSESSIONID")
        //        .logoutSuccessUrl("/");

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/home", "/recipes", "/register", "/recipes/info/**").permitAll()
                //.antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .failureUrl("/login?error=BadCredentials")
                .defaultSuccessUrl("/home", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login");
                //.and()
                //.exceptionHandling().accessDeniedPage("/access_denied");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.customUsernamePasswordAuthenticationProvider);
    }


}
