package xyz.celinski;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Connection connection;

    private DatabaseConnection() {
        try (FileReader fileReader = new FileReader("src/main/resources/db_config.json")) {
            JSONTokener jsonTokener = new JSONTokener(fileReader);
            JSONObject secrets = new JSONObject(jsonTokener);

            String url = secrets.getString("url");
            String user = secrets.getString("user");
            String password = secrets.getString("password");

            connection = DriverManager.getConnection(url, user, password);
        }
        catch (IOException e) {
            throw new RuntimeException("Can't find 'db_config.json' file");
        }
        catch (SQLException e) {
            throw new RuntimeException("Can't connect to database");
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void closeConnection() {
        try {
            connection.close();
            instance = null;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }

    public int executeUpdate(String sql) throws SQLException {
        return connection.createStatement().executeUpdate(sql);
    }
}
