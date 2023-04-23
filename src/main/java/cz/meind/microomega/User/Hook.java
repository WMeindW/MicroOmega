package cz.meind.microomega.User;

import java.io.Serializable;
import java.time.LocalTime;

public class Hook implements Serializable {
    private String text;
    private LocalTime time;
    private User sender;

    public Hook(String text, User sender) {
        this.text = text;
        time = LocalTime.now();
        this.sender = sender;
    }

    public User getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public LocalTime getTime() {
        return time;
    }
}
