package model;

public class User {
    private String userId;
    private String username;
    private String password;
    private String name;
    private String email;
    private String personal_info;

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonal_info() {
        return personal_info;
    }

    public void setPersonal_info(String personal_info) {
        this.personal_info = personal_info;
    }
}
