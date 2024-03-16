package xyz.celinski;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnection {
    private static DatabaseConnection instance;
    private String url;
    private String username;
    private String password;

    private DatabaseConnection() {
        final String CONFIG_FILE_PATH = "resources\\config.properties";
        File configFile = new File(CONFIG_FILE_PATH);
        loadConfig(configFile);
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private void loadConfig(File configFile) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(configFile)) {
            properties.load(input);
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, username, password);
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(Connection connection) {
        try {
            connection.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
