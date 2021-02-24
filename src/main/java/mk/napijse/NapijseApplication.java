package mk.napijse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class NapijseApplication {

    public static void main(String[] args) {
        SpringApplication.run(NapijseApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
    @Value("${email.host}")
    private String host;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Value("${email.port}")
    private Integer port;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(this.host);
        javaMailSender.setPort(this.port);
        javaMailSender.setUsername(this.username);
        javaMailSender.setPassword(this.password);
        return javaMailSender;
    }
}
