package xyz.celinski;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserById(Integer id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
}
