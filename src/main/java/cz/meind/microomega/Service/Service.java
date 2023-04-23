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
        form = Objects.requireNonNull(form).replace("@0", "id=" + Objects.requireNonNull(user).getId());
        form = Objects.requireNonNull(form).replace("@1", data.split("username=")[1]);
        return form;
    }

    public static String info(String data) {
        User user = getUser(data);
        String form = Database.read("components/info-form-card.html");
        form = Objects.requireNonNull(form).replace("@0", "id=" + Objects.requireNonNull(user).getId());
        form = Objects.requireNonNull(form).replace("@1", "id=" + Objects.requireNonNull(user).getId());
        form = Objects.requireNonNull(form).replace("@2", "id=" + Objects.requireNonNull(user).getId());
        String section = Database.read("components/info-section-card.html");
        String sections = "";
        sections += Objects.requireNonNull(section).replace("@0", "Username").replace("@1", "username").replace("@2", user.getUserName());
        sections += section.replace("@0", "Password").replace("@1", "password").replace("@2", user.getPassword());
        sections += section.replace("@0", "Bio").replace("@1", "bio").replace("@2", user.getBioProfile());
        form = Objects.requireNonNull(form).replace("@3", sections);
        return form;
    }

    public static String friends(String data) {
        User user = getUser(data);
        String card = Database.read("components/user-card.html");
        StringBuilder friendCards = new StringBuilder();
        for (User friend : user.getFriends()) {
            friendCards.append(Objects.requireNonNull(card).replace("@0", friend.getUserName()).replace("@1", "id=" + friend.getId()).replace("@2", friend.getUserName()).replace("@3", friend.getLastActive().toString()));
        }
        return friendCards.toString();
    }

    public static String query(String data) {
        String card = Database.read("components/query-user-card.html");
        String query = data.split("query=")[1].split(",")[0];
        ArrayList<User> users = new ArrayList<>();
        if (Service.getUser(data) != null) {
            for (User candidate : Database.deserializeAndRead()) {
                if (candidate.getUserName().toLowerCase().contains(query.toLowerCase()) || candidate.getBioProfile().toLowerCase().contains(query.toLowerCase())) {
                    users.add(candidate);
                }
            }
        }
        StringBuilder html = new StringBuilder();
        for (User user : users) {
            html.append(Objects.requireNonNull(card).replace("@0", "id=" + user.getId()).replace("@1", user.getUserName()).replace("@2", "id=" + user.getId()));
        }
        return html.toString();
    }

    public static boolean add(String data) {
        User user = getUser(data);
        User addUser = Database.userId(data.split("addId=")[1].split(",")[0]);
        if (addUser != null && user != null) {
            addUser.getFriends().add(user);
            user.getFriends().add(addUser);
            return true;
        }
        return false;
    }

    public static boolean send(String data) {
        User user = getUser(data);
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
        Exchange in = null;
        for (Exchange exchange : list) {
            if (exchange.getTwo().equals(sent) && exchange.getOne().equals(sent) || exchange.getTwo().equals(received) && exchange.getOne().equals(received)) {
                in = exchange;
            }
        }
        String sentCard = Database.read("components/message-sent.html");
        String receivedCard = Database.read("components/message-received.html");
        StringBuilder html = new StringBuilder();
        if (in != null) {
            for (Hook message : in.getMessages()) {
                if (message.getSender().equals(sent))
                    html.append(Objects.requireNonNull(sentCard).replace("@0", message.getText()).replace("@1", message.getTime().toString()));
                else if (message.getSender().equals(received))
                    html.append(Objects.requireNonNull(receivedCard).replace("@0", message.getText()).replace("@1", message.getTime().toString()));
            }
        }
        return html.toString();
    }
}
