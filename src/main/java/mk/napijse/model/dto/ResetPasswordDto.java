package mk.napijse.model.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String username;
    private String email;
    private String password;
    private String repeatPassword;
    private String token;
}
