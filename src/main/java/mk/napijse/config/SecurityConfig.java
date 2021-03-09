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
        web.ignoring().antMatchers("/resources/**", "/static/**",
                "/images/**", "/webjars/**", "/**.png", "/**.jpg", "/**.svg");
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
                .antMatchers("/","/h2" ,"/home", "/recipes", "/register", "/register/verify",
                        "/recipes/info/**", "/recipes/search", "/about-us", "/**/recipes", "/password/change",
                        "/forgot-password", "/password/request").permitAll()
                .antMatchers("/event").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/login/post")
                .failureUrl("/login?error=BadCredentials")
                .defaultSuccessUrl("/home", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.customUsernamePasswordAuthenticationProvider);
    }


}
