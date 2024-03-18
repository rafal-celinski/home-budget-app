package xyz.celinski;

import xyz.celinski.exceptions.RepositoryAccessException;
import xyz.celinski.exceptions.UserAlreadyExists;
import xyz.celinski.exceptions.UserDoesntExists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    DatabaseConnection database;

    public UserRepositoryImpl() {
        database = DatabaseConnection.getInstance();
    }

    private Optional<User> getUserBy(String columnName, Object value) throws IllegalArgumentException, RepositoryAccessException {
        Connection connection = database.getConnection();
        String sql = "SELECT * FROM users WHERE " + columnName + " = ?;";

        System.out.println(sql);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            if (value instanceof Integer)
                statement.setInt(1, (Integer) value);
            else if (value instanceof String)
                statement.setString(1, (String) value);
            else
                throw new IllegalArgumentException("Value must be Integer or String. Given: " + value.getClass().getName());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Integer userId = result.getInt("user_id");
                String username = result.getString("username");
                String passwordHash = result.getString("password_hash");
                String email = result.getString("email");
                Date dateCreated = result.getDate("date_created");
                return Optional.of(new User(userId, username, passwordHash, email, dateCreated));
            }
        }
        catch (SQLException e) {
            throw new RepositoryAccessException("Cannot reach repository. " + e.getMessage());
        }
        finally {
            database.close(connection);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Integer id) throws RepositoryAccessException {
        try {
            return getUserBy("user_id", id);
        }
        catch (IllegalArgumentException e) {
            throw new RepositoryAccessException("Cannot reach repository. " + e.getMessage());
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws RepositoryAccessException {
        try {
            return getUserBy("username", username);
        }
        catch (IllegalArgumentException e) {
            throw new RepositoryAccessException("Cannot reach repository. " + e.getMessage());
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws RepositoryAccessException {
        try {
            return getUserBy("email", email);
        }
        catch (IllegalArgumentException e) {
            throw new RepositoryAccessException("Cannot reach repository. " + e.getMessage());
        }
    }

    @Override
    public void addUser(User user) throws UserAlreadyExists, RepositoryAccessException {
        if (user == null)
            throw new NullPointerException();

        Connection connection = database.getConnection();
        String sql = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?);";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23"))
                throw new UserAlreadyExists("User with given username or/and email already exists");
            else {
                throw new RepositoryAccessException("Cannot reach repository");
            }
        }
        finally {
            database.close(connection);
        }
    }

    @Override
    public void updateUser(User user) throws UserDoesntExists, UserAlreadyExists, RepositoryAccessException {
        if (user == null)
            throw new NullPointerException();

        Connection connection = database.getConnection();
        String sql = "UPDATE users SET username = ?, password_hash = ?, email = ? WHERE user_id = ?";
        int affectedRows;

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getUserId());
            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23"))
                throw new UserAlreadyExists("User with given username or/and email already exists");
            else {
                throw new RepositoryAccessException("Cannot reach repository");
            }
        }
        finally {
            database.close(connection);
        }

        if (affectedRows == 0)
            throw new UserDoesntExists("User doesn't exist in repository");
    }

    @Override
    public void deleteUser(User user) throws RepositoryAccessException, UserDoesntExists {
        if (user == null)
            throw new NullPointerException();

        Connection connection = database.getConnection();
        String sql = "DELETE FROM users WHERE user_id = ?";
        int affectedRows;

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getUserId());
            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryAccessException("Cannot reach repository");
        }
        finally {
            database.close(connection);
        }
        if (affectedRows == 0)
            throw new UserDoesntExists("User doesn't exist in repository");
    }
}
