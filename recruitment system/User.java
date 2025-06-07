package rms;
import java.io.*;

abstract class User implements  Serializable{
    private static final long serialVersionUID = 1L;
    protected int user_id;
    protected String name;
    protected String email;
    protected String password;
    
    public User(int user_id, String name, String email, String password) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    public abstract void logIn(String inputEmail, String inputPassword) throws LoginException;
    public abstract void updateProfile();
    
    public int getUserId() { return user_id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    @Override
    public String toString() {
    return "UserID: " + user_id + ", Name: " + name + ", Email: " + email;
}

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}