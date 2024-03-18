package xyz.celinski;

import xyz.celinski.exceptions.RepositoryAccessException;
import xyz.celinski.exceptions.UserAlreadyExists;
import xyz.celinski.exceptions.UserDoesntExists;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserById(Integer id) throws RepositoryAccessException;
    Optional<User> getUserByUsername(String username) throws RepositoryAccessException;
    Optional<User> getUserByEmail(String email) throws RepositoryAccessException;
    void addUser(User user) throws UserAlreadyExists, RepositoryAccessException;
    void updateUser(User user) throws UserDoesntExists, UserAlreadyExists, RepositoryAccessException;
    void deleteUser(User user) throws RepositoryAccessException, UserDoesntExists;
}
