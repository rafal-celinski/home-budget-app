package xyz.celinski;

import java.util.Date;
import java.util.Optional;

public class User {
    Optional<Integer> user_id;
    String username;
    String password_hash;
    String email;
    Date data_created;

    public User(Integer user_id, String username, String password_hash, String email, Date data_created) {
        this.user_id = Optional.of(user_id);
        this.username = username;
        this.password_hash = password_hash;
        this.email = email;
        this.data_created = data_created;
    }

    public User(String username, String password_hash, String email) {
        this.username = username;
        this.password_hash = password_hash;
        this.email = email;
    }

    public Optional<Integer> getUser_id() {
        return user_id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getData_created() {
        return data_created;
    }

    public void setData_created(Date data_created) {
        this.data_created = data_created;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", email='" + email + '\'' +
                ", data_created=" + data_created +
                '}';
    }
}
