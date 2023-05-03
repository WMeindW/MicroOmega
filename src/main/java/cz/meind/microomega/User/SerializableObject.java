package cz.meind.microomega.User;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class SerializableObject implements Serializable {
    public LocalTime time;
    public ArrayList<User> list;
    public UType type;
    public byte[] profilePicture;

    public SerializableObject(LocalTime time, ArrayList<User> list, UType type, byte[] profilePicture) {
        this.time = time;
        this.list = list;
        this.type = type;
        this.profilePicture = profilePicture;
    }
}
