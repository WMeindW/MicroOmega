package cz.meind.microomega.Service;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.User.UType;
import cz.meind.microomega.User.User;

import java.util.ArrayList;

public class Register {
    public static boolean register(String body) {
        User user = new User(UType.USER, body.split("&")[0].split("=")[1], body.split("&")[1].split("=")[1]);
        ArrayList<User> list = Database.deserializeAndRead();
        if (user.getUserName().length() < 5) return false;
        if (user.getPassword().length() < 5) return false;
        for (User value : list)
            if (value.getUserName().equals(user.getUserName())) return false;
        return Database.serializeAndWrite(user);
    }
}
