package cz.meind.microomega.User;

import java.io.Serializable;
import java.time.LocalTime;

public class Hook implements Serializable {
    private String text;
    private LocalTime time;
    private User sender;
    private User receiver;

    public Hook(String text, User sender, User receiver) {
        time = LocalTime.now();
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Hook(String text, LocalTime time, User sender, User receiver) {
        this.text = text;
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
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
    public User getReceiver() {
        return receiver;
    }

    @Override
    public String toString() {
        return "Hook{" +
                "text='" + text + '\'' +
                ", time=" + time +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
