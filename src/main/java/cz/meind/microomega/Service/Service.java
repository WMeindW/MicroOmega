package cz.meind.microomega.Service;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.User.Hook;
import cz.meind.microomega.User.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class Service {
    private static User getUser(String data) {
        String ssnId = data.split("id=")[1].split("&")[0];
        HashMap<String, String> map = Database.readIds();
        String[] names = map.keySet().toArray(new String[0]);
        String[] ids = map.values().toArray(new String[0]);
        User user = null;
        for (int i = 0; i < names.length; i++) {
            if (ids[i].equals(ssnId)) user = Database.userName(names[i]);
        }
        if (user == null) user = Database.userId(ssnId);
        return user;
    }

    public static String sendHook(String data) {
        User user = getUser(data);
        User user1 = Database.userName(data.split("username=")[1]);
        if (user == null || user1 == null) return null;
        if (user.getFriends().contains(user1)) return "id=" + user.getId() + "&" + user1.getUserName();
        return null;
    }

    public static String info(String data) {
        User user = getUser(data);
        if (user == null) return null;
        return "id=" + user.getId() + "&" + user.getUserName() + "&" + user.getPassword() + "&" + user.getBioProfile();
    }

    public static String friends(String data) {
        User user = getUser(data);
        if (user == null) return null;
        StringBuilder friends = new StringBuilder();
        for (User friend : user.getFriends()) {
            friends.append(friend.getUserName()).append("&").append("id=").append(friend.getId()).append("&").append(friend.getLastActive().getHour()).append(":").append(friend.getLastActive().getMinute()).append("#");
        }
        return friends.toString();
    }

    public static String query(String data) {
        if (data.split("query=").length > 1) {
            String query = data.split("query=")[1].split(",")[0];
            ArrayList<User> users = new ArrayList<>();
            User user = getUser(data);
            StringBuilder html = new StringBuilder();
            if (user != null) {
                for (User candidate : Database.deserializeAndRead()) {
                    if (!user.getFriends().contains(candidate) && !user.equals(candidate)) {
                        if (candidate.getUserName().toLowerCase().contains(query.toLowerCase()) || candidate.getBioProfile().toLowerCase().contains(query.toLowerCase())) {
                            users.add(candidate);
                        }
                    }
                }
                for (User match : users) {
                    html.append("id=").append(match.getId()).append("&").append(match.getUserName()).append("#");
                }
                return html.toString();
            }
        }
        return "";
    }

    public static String add(String data) {
        User user = getUser(data);
        if (user == null) return null;
        User candidate = Database.userId(data.split("addId=id=")[1]);
        if (candidate != null && !candidate.getFriends().contains(user) && !user.getFriends().contains(candidate)) {
            candidate.getFriends().add(user);
            user.getFriends().add(candidate);
            Database.editUser(user);
            Database.editUser(candidate);
            return "success";
        }
        return "failed";
    }

    public static String send(String data) {
        User user = getUser(data);
        User user1 = Database.userName(data.split("username=")[1].split("&")[0]);
        String text = data.split("text=")[1].split("&")[0];
        if (user == null || user1 == null) return "failed";
        Hook hook = new Hook(text, user, user1);
        Database.serializeAndWriteHooks(hook);
        return "success";
    }

    public static String messages(String data) {
        User user = getUser(data);
        User user1 = Database.userName(data.split("username=")[1].split("&")[0]);
        HashMap<ArrayList<User>, LinkedList<Hook>> map = Database.deserializeAndReadHooks();
        ArrayList<User> list = new ArrayList<>();
        list.add(user);
        list.add(user1);
        LinkedList<Hook> hooks = map.get(list);
        Collections.shuffle(list);
        if (hooks == null) hooks = map.get(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hooks.size(); i++) {
            sb.append("#");
            if (hooks.get(i).getSender().equals(user)) {
                sb.append("1").append(hooks.get(i).getText()).append("&").append(hooks.get(i).getTime());
            } else if (hooks.get(i).getSender().equals(user1)) {
                sb.append("0").append(hooks.get(i).getText()).append("&").append(hooks.get(i).getTime());
            }
        }
        return sb.toString().replaceFirst("#", "");
    }

    public static String user(String data) {
        User user = getUser(data);
        if (user == null) return null;
        return "id=" + user.getId() + "&" + user.getUserName();
    }

    public static byte[] profile(String data) {
        User user = Database.userId(data);
        if (user != null) {
            return user.getProfilePicture();
        }
        return new byte[0];
    }

    public static boolean logout(String data) {
        User user = Database.userId(data);
        HashMap<String, String> map = Database.readIds();
        if (user != null) {
            map.remove(user.getUserName());
            Database.writeIds(map);
            return true;
        }
        return false;
    }

    public static String edit(String body) {
        User user = Database.userId(body.replace("id=", "").split("&")[0]);
        if (user != null) {
            user.setUserName(body.split("username=")[1].split("&")[0]);
            user.setPassword(body.split("password=")[1].split("&")[0]);
            user.setBioProfile(body.split("bio=")[1].split("&")[0]);
            Database.editUser(user);
            return "success";
        }
        return "fail";
    }

}
