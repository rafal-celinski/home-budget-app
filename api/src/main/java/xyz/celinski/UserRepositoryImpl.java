package xyz.celinski;

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

    private Optional<User> getUserBy(String columnName, Object value) {
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
                throw new IllegalArgumentException("value must be Integer or String");

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Integer userId = result.getInt("user_id");
                String username = result.getString("username");
                String passwordHash = result.getString("password_hash");
                String email = result.getString("email");
                Date dateCreated = result.getDate("date_created");
                return Optional.of(new User(userId, username, passwordHash, email, dateCreated));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return getUserBy("user_id", id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return getUserBy("username", username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return getUserBy("email", email);
    }

    @Override
    public void addUser(User user) {
        Connection connection = database.getConnection();
        String sql = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?);";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        Connection connection = database.getConnection();
        String sql = "UPDATE users SET username = ?, password_hash = ?, email = ? WHERE user_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(User user) {
        Connection connection = database.getConnection();
        String sql = "DELETE FROM users WHERE user_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
