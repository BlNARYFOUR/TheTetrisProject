package data.loginRepository;

import domain.User;

/**
 * Contract for a loginRepository.
 */
public interface LoginRepository {
    void addUser(User u);
    User authenticateUser(String username, String password);
    User authenticateUser(User user);
    User authenticateUser(String username, String password, boolean hashPass);
    User deleteUser(String username);
    User getUser(String username);

}
