package mk.napijse.service;

import mk.napijse.model.User;

public interface AuthenticationService {

        User login(String username, String password);


}
