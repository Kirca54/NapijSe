package mk.napijse.service;

import mk.napijse.model.entities.User;

public interface AuthenticationService {

        User login(String username, String password);


}
