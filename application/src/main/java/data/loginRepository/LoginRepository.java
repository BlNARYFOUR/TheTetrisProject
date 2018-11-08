package data.loginRepository;

import domain.User;

public interface LoginRepository {
    void addUser(User u);
    User authenticateUser(String username, String password);
    User authenticateUser(User user);
    User deleteUser(String username);
    User getUser(String username);

}
