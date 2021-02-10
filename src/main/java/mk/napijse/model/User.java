package mk.napijse.model;

import lombok.Data;
import mk.napijse.model.enumerations.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    private String username;
    private String password;

    private String name;
    private String surname;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToMany
    private List<Recipe> favourites;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.EAGER)
    private List<Recipe> recipes;

    public User(String name, String surname, String username, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.role = role;
        this.favourites = new ArrayList<>();
    }

    public User() {
    }
}
