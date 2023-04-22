package cz.meind.microomega.User;

import java.time.LocalTime;

public class Hook {
    private String message;
    private LocalTime time;

    public Hook(String message) {
        this.message = message;
        time = LocalTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
