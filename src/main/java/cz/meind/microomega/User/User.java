package cz.meind.microomega.User;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private UType type;
    private String userName;
    private String password;
    public User(UType type, String userName, String password) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return type == user.type && Objects.equals(userName, user.userName) && Objects.equals(password, user.password);
    }

    @Override
    public String toString() {
        return "User{" + "type=" + type + ", userName='" + userName + '\'' + ", password='" + password + '\'' + '}';
    }
}
