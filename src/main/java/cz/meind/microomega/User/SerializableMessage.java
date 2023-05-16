package cz.meind.microomega.User;

import java.io.Serializable;
import java.time.LocalTime;

public class SerializableMessage implements Serializable {
    public String text;
    public LocalTime time;
    public User sender;

    public SerializableMessage(String text, LocalTime time, User sender) {
        this.text = text;
        this.time = time;
        this.sender = sender;
    }
}
