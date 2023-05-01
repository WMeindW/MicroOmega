package cz.meind.microomega.Service;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.User.UType;
import cz.meind.microomega.User.User;
import jakarta.servlet.http.Cookie;

import java.util.ArrayList;

public class Login {

    public static String login(String body) {
        User user = new User(UType.USER, body.split("&")[0].split("=")[1], body.split("&")[1].split("=")[1]);
        ArrayList<User> list = Database.deserializeAndRead();
        if (user.getUserName().length() < 5) return null;
        if (user.getPassword().length() < 5) return null;
        for (User value : list)
            if (value.getPassword().equals(user.getPassword()) && user.getUserName().equals(value.getUserName()))
                return Database.generate(user);
        return null;
    }

    public static boolean checkCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            for (String id : Database.readIds().values()) {
                if (cookie.getValue().equals(id)) return true;
            }
        }
        return false;
    }
}
