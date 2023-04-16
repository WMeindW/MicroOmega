package cz.meind.microomega.User;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class User implements Serializable {
    private Random random = new Random();
    private UType type;
    private String userName;
    private String password;
    private byte[] profilePicture;
    private String bioProfilePicture;

    private String id;

    public User(UType type, String userName, String password) {
        this.type = type;
        this.userName = userName;
        this.password = password;
        id = "USRID-" + ((double) random.nextInt(1, 9999) / random.nextInt(1, 9999));
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public String getId() {
        return id;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBioProfilePicture() {
        return bioProfilePicture;
    }

    public void setBioProfilePicture(String bioProfilePicture) {
        this.bioProfilePicture = bioProfilePicture;
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
