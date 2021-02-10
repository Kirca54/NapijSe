package mk.napijse.service;

import mk.napijse.model.User;
import mk.napijse.model.enumerations.Role;

public interface UserService {

    User register(String username, String password, String repeatPassword, String name, String surname, Role role);

}
