package mk.napijse.repository;

import mk.napijse.model.entities.User;
import mk.napijse.model.enumerations.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    List<User> findAllByRole(Role role);
}
