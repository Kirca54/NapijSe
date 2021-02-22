package mk.napijse.model;

import lombok.Data;
import mk.napijse.model.enumerations.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private String username;
    private String password;

    private String name;
    private String surname;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToMany
    private List<Recipe> favourites;

    @OneToMany(mappedBy = "recipeUser", fetch = FetchType.EAGER)
    private List<Recipe> recipes;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    public User(String name, String surname, String email, String username, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.favourites = new ArrayList<>();
    }

    public User() {
    }

    public void addToFavourites(Recipe recipe){
        this.favourites.add(recipe);
    }

    public void deleteFromFavourites(Recipe recipe){
        this.favourites.remove(recipe);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
