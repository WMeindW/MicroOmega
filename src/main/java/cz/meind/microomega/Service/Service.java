package cz.meind.microomega.Service;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.User.Exchange;
import cz.meind.microomega.User.Hook;
import cz.meind.microomega.User.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
        if (user == null) return null;
        return "id=" + Objects.requireNonNull(user).getId() + "&" + data.split("username=")[1];
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
        User candidate = Database.userId(data.split("addId=id%3D")[1]);
        if (candidate != null && !candidate.getFriends().contains(user) && !user.getFriends().contains(candidate)) {
            candidate.getFriends().add(user);
            user.getFriends().add(candidate);
            Database.editUser(user);
            Database.editUser(candidate);
            return "success";
        }
        return "failed";
    }

    public static boolean send(String data) {
        User user = getUser(data);
        if (user == null) return false;
        Hook hook = new Hook(data.split("message=")[1], user);
        String username = data.split("username=")[1].split(",")[0];
        ArrayList<Exchange> list = Database.deserializeAndReadExchange();
        for (Exchange exchange : list) {
            if (exchange.getTwo().equals(user) && exchange.getOne().getUserName().equals(username) || exchange.getTwo().getUserName().equals(username) && exchange.getOne().equals(user)) {
                exchange.getMessages().add(hook);
                return Database.editExchange(exchange);
            }
        }
        Exchange exchange = new Exchange(user, Database.userName(username));
        exchange.getMessages().add(hook);
        list.add(exchange);
        return false;
    }

    public static String messages(String data) {
        ArrayList<Exchange> list = Database.deserializeAndReadExchange();
        User sent = getUser(data);
        User received = Database.userName(data.split("username=")[1].split(",")[0]);
        if (sent == null || received == null) return null;
        Exchange in = null;
        for (Exchange exchange : list) {
            if (exchange.getTwo().equals(sent) && exchange.getOne().equals(sent) || exchange.getTwo().equals(received) && exchange.getOne().equals(received)) {
                in = exchange;
            }
        }
        StringBuilder html = new StringBuilder();
        if (in != null) {
            for (Hook message : in.getMessages()) {
                if (message.getSender().equals(sent))
                    html.append(1).append(message.getText()).append(message.getTime().toString()).append("#");
                else if (message.getSender().equals(received))
                    html.append(0).append(message.getText()).append(message.getTime().toString()).append("#");
            }
        }
        return html.toString();
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
        User user = Database.userId(body.split("id=")[1].split("&")[0].replace("id%3D", ""));
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
