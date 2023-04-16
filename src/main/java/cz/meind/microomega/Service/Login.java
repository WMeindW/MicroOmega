package cz.meind.microomega.Service;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.User.UType;
import cz.meind.microomega.User.User;

import java.util.ArrayList;

public class Login {

    public static String login(String body) {
        User user = new User(UType.USER, body.split("&")[0].split("=")[1], body.split("&")[1].split("=")[1]);
        ArrayList<User> list = Database.deserializeAndRead();
        for (User value : list)
            if (value.getPassword().equals(user.getPassword())) return Database.generate(user);
        return null;
    }

    public static boolean checkCookie(String cookie) {
        for (String value : Database.readIds().values())
            if (value.equals(cookie)) return true;
        return false;
    }
}
