package cz.meind.microomega.Service;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.User.User;

import java.util.HashMap;
import java.util.Objects;

public class Service {
    private static User getUser(String data) {
        String id = data.substring(3);
        HashMap<String, String> map = Database.readIds();
        User user = null;
        for (String ssnid : map.values()) {
            if (ssnid.equals(id)) {
                user = Database.userName(map.get(ssnid));
            }
        }
        return user;
    }

    public static String sendHook(String data) {
        User user = getUser(data);
        String form = Database.read("components/form-card.html");
        form = Objects.requireNonNull(form).replace("@0",
                "id=" + Objects.requireNonNull(user).getId());
        return form;
    }

    public static String info(String data) {
        User user = getUser(data);
        String form = Database.read("components/info-form-card.html");
        form = Objects.requireNonNull(form).replace("@0",
                "id=" + Objects.requireNonNull(user).getId());
        form = Objects.requireNonNull(form).replace("@1",
                "id=" + Objects.requireNonNull(user).getId());
        form = Objects.requireNonNull(form).replace("@2",
                "id=" + Objects.requireNonNull(user).getId());
        String section = Database.read("components/info-section-card.html");
        String sections = "";
        sections += Objects.requireNonNull(section).replace("@0", "Username")
                .replace("@1", "username")
                .replace("@2", user.getUserName());
        sections += section.replace("@0", "Password")
                .replace("@1", "password")
                .replace("@2", user.getPassword());
        sections += section.replace("@0", "Bio")
                .replace("@1", "bio")
                .replace("@2", user.getBioProfile());
        form = Objects.requireNonNull(form).replace("@3", sections);
        return form;
    }

    public static String friends(String data) {
        User user = getUser(data);
        String card = Database.read("components/user-card.html");
        for (User friend : user.getFriends()) {

        }
    }
}
