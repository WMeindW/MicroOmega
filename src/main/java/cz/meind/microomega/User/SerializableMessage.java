package cz.meind.microomega.User;

import java.io.Serializable;
import java.time.LocalTime;

public class SerializableMessage implements Serializable {
    public String message;
    public LocalTime time;
}
