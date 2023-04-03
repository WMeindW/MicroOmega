package cz.meind.microomega.Service;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.User.UType;
import cz.meind.microomega.User.User;

import java.util.ArrayList;

public class Register {
    public static boolean register(String body) {
        User user = User.instance(UType.USER, body.split("&")[0].split("=")[1], body.split("&")[1].split("=")[1]);
        ArrayList<User> list = Database.deserializeAndRead();
        System.out.println(list);
        for (User value : list)
            if (value.getUserName().equals(user.getUserName())) return false;
        return Database.serializeAndWrite(user);
    }
}
