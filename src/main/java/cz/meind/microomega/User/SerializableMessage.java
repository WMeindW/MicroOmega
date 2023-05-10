package cz.meind.microomega.User;

import java.io.Serializable;
import java.time.LocalTime;

public class SerializableMessage implements Serializable {
    public String text;
    public LocalTime time;

    public SerializableMessage(String text, LocalTime time) {
        this.text = text;
        this.time = time;
    }
}
