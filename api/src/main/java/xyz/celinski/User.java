package xyz.celinski;

import java.util.Date;

public class User {
    private Integer userId;
    private String username;
    private String passwordHash;
    private String email;
    private Date dateCreated;

    public User(Integer user_id, String username, String password_hash, String email, Date data_created) {
        this.userId = user_id;
        this.username = username;
        this.passwordHash = password_hash;
        this.email = email;
        this.dateCreated = data_created;
    }

    public User(String username, String password_hash, String email) {
        this.username = username;
        this.passwordHash = password_hash;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
