package cz.meind.microomega.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class User implements Serializable {
    private Random random = new Random();
    private UType type;
    private String userName;
    private String password;
    private byte[] profilePicture;
    private String bioProfile;
    private String id;
    private ArrayList<User> friends;
    private LocalDateTime lastActive;

    public User(UType type, String userName, String password) {
        this.type = type;
        this.userName = userName;
        this.password = password;
        id = "USRID-" + (random.nextInt(99) + (double) random.nextInt(1, 9999) / random.nextInt(1, 9999));
        friends = new ArrayList<>();
        lastActive = LocalDateTime.now();
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    public void setActive() {
        lastActive = LocalDateTime.now();
    }

    public ArrayList<User> getFriends() {
        return friends;
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

    public String getBioProfile() {
        return bioProfile;
    }

    public void setBioProfile(String bioProfile) {
        this.bioProfile = bioProfile;
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
