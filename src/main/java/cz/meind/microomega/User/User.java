package cz.meind.microomega.User;

import java.io.Serializable;

public class User implements Serializable {
    private UType type;
    private String userName;
    private String password;
    private static User instance;

    public static User instance(UType type, String userName, String password) {
        if (instance == null) {
            instance = new User(type, userName, password);
        }
        return instance;
    }

    private User(UType type, String userName, String password) {
        this.type = type;
        this.userName = userName;
        this.password = password;
    }

    public UType getType() {
        return type;
    }

    public void setType(UType type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "type=" + type +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
