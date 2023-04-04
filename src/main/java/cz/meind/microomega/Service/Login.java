package cz.meind.microomega.Service;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.User.UType;
import cz.meind.microomega.User.User;

import java.util.ArrayList;

public class Login {

    public static boolean login(String body) {
        User user = new User(UType.USER, body.split("&")[0].split("=")[1], body.split("&")[1].split("=")[1]);
        ArrayList<User> list = Database.deserializeAndRead();
        for (User value : list)
            if (value.getPassword().equals(user.getPassword())) return true;
        return false;
    }
}
