package cz.meind.microomega.Database;

import cz.meind.microomega.User.Hook;
import cz.meind.microomega.User.SerializableMessage;
import cz.meind.microomega.User.SerializableObject;
import cz.meind.microomega.User.User;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Database {
    public static String read(String path) {
        StringBuilder out = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File("src/main/resources/static/" + path));
            while (scanner.hasNext()) out.append(scanner.nextLine());
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return out.toString();
    }

    public static boolean serializeAndWrite(User user) {
        File file = new File("src/main/java/cz/meind/microomega/Database/Files/files.dat");
        FileWriter writer;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(new SerializableObject(user.getLastActive(), user.getFriends(), user.getType(), user.getProfilePicture()));
            writer.append("username=").append(URLEncoder.encode(user.getUserName(), StandardCharsets.UTF_8)).append("&").append("password=").append(URLEncoder.encode(user.getPassword(), StandardCharsets.UTF_8)).append("&").append("bio=").append(URLEncoder.encode(user.getBioProfile(), StandardCharsets.UTF_8)).append("&").append("id=").append(user.getId()).append("&").append("obj=").append(Base64.getEncoder().encodeToString(out.toByteArray())).append('\n');
            writer.close();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static ArrayList<User> deserializeAndRead() {
        File file = new File("src/main/java/cz/meind/microomega/Database/Files/files.dat");
        ArrayList<User> users = new ArrayList<>();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return users;
            }
        }
        Scanner scanner;
        try {
            scanner = new Scanner(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNext()) {
            String line = scanner.next();
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(line.split("obj=")[1]));
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                SerializableObject obj = (SerializableObject) ois.readObject();
                users.add(new User(obj.type, URLDecoder.decode(line.split("username=")[1].split("&")[0], StandardCharsets.UTF_8), URLDecoder.decode(line.split("password=")[1].split("&")[0], StandardCharsets.UTF_8), obj.profilePicture, URLDecoder.decode(line.split("bio=")[1].split("&")[0], StandardCharsets.UTF_8), line.split("id=")[1].split("&")[0], obj.list, obj.time));
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return users;
    }

    public static String generate(User user) {
        HashMap<String, String> map = readIds();
        Random random = new Random();
        map.remove(user.getUserName());
        String id = "SSNID-" + ((double) random.nextInt(1, 9999) / random.nextInt(1, 9999));
        while (map.containsValue(id)) {
            id = "SSNID-" + ((double) random.nextInt(1, 9999) / random.nextInt(1, 9999));
        }
        map.put(user.getUserName(), id);
        if (writeIds(map)) return id;
        return null;
    }

    public static HashMap<String, String> readIds() {
        File file = new File("src/main/java/cz/meind/microomega/Database/Files/idLog.dat");
        HashMap<String, String> idLogs = new HashMap<>();
        Scanner scanner;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return idLogs;
            }
        }
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            idLogs.put(line.split(",")[0].replaceAll(",", ""), line.split(",")[1].replaceAll(",", ""));
        }
        return idLogs;
    }

    public static boolean writeIds(HashMap<String, String> idLogs) {
        File file = new File("src/main/java/cz/meind/microomega/Database/Files/idLog.dat");
        FileWriter writer;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            return false;
        }
        for (int i = 0; i < idLogs.size(); i++) {
            try {
                writer.append(String.valueOf(idLogs.keySet().toArray()[i])).append(",").append(String.valueOf(idLogs.values().toArray()[i])).append("\n");
            } catch (IOException e) {
                return false;
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static User userId(String id) {
        for (User user : deserializeAndRead()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public static User userName(String name) {
        for (User user : deserializeAndRead()) {
            if (user.getUserName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public static boolean editUser(User user) {
        ArrayList<User> list = deserializeAndRead();
        for (User user1 : list) {
            if (user1.equals(user)) {
                if (!user1.getUserName().equals(user.getUserName())) {
                    HashMap<String, String> map = readIds();
                    String[] names = map.keySet().toArray(new String[0]);
                    for (String name : names) {
                        if (name.equals(user1.getUserName())) {
                            String id = map.get(name);
                            map.remove(name);
                            map.put(user.getUserName(), id);
                            writeIds(map);
                        }
                    }
                }
                list.remove(user1);
                list.add(user);
                File file = new File("src/main/java/cz/meind/microomega/Database/Files/files.dat");
                FileWriter writer;
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                try {
                    writer = new FileWriter(file, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                StringBuilder content = new StringBuilder();
                for (User u : list) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(out);
                        oos.writeObject(new SerializableObject(u.getLastActive(), u.getFriends(), u.getType(), u.getProfilePicture()));
                        content.append("username=").append(URLEncoder.encode(u.getUserName(), StandardCharsets.UTF_8)).append("&").append("password=").append(URLEncoder.encode(u.getPassword(), StandardCharsets.UTF_8)).append("&").append("bio=").append(URLEncoder.encode(u.getBioProfile(), StandardCharsets.UTF_8)).append("&").append("id=").append(u.getId()).append("&").append("obj=").append(Base64.getEncoder().encodeToString(out.toByteArray())).append('\n');
                        oos.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    writer.append(content.toString());
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean serializeAndWriteHooks(Hook hook) {
        File file = new File("src/main/java/cz/meind/microomega/Database/Files/messages.dat");
        FileWriter writer;
        HashMap<ArrayList<User>, LinkedList<Hook>> map = Database.deserializeAndReadHooks();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean contains = false;
        List<ArrayList<User>> users = map.keySet().stream().toList();
        List<LinkedList<Hook>> hooks = map.values().stream().toList();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).contains(hook.getSender()) && users.get(i).contains(hook.getReceiver())) {
                hooks.get(i).add(hook);
                map = new HashMap<>();
                contains = true;
                for (int j = 0; j < users.size(); j++) {
                    map.put(users.get(i), hooks.get(i));
                }
            }
        }
        if (!contains) {
            ArrayList<User> list = new ArrayList<>();
            LinkedList<Hook> list0 = new LinkedList<>();
            list0.add(hook);
            list.add(hook.getSender());
            list.add(hook.getReceiver());
            map.put(list, list0);
        }
        users = map.keySet().stream().toList();
        hooks = map.values().stream().toList();
        for (int i = 0; i < map.size(); i++) {
            StringBuilder line = new StringBuilder("usr=" + users.get(i).get(0).getId() + "," + users.get(i).get(1).getId() + "msg=");
            for (int j = 0; j < hooks.get(i).size(); j++) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(bout);
                    oos.writeObject(new SerializableMessage(hooks.get(i).get(j).getText(), hooks.get(i).get(j).getTime(), hooks.get(i).get(j).getSender()));
                    line.append(Base64.getEncoder().encodeToString(bout.toByteArray())).append(",");
                    oos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                writer.append(URLEncoder.encode(line.toString(), StandardCharsets.UTF_8)).append("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static HashMap<ArrayList<User>, LinkedList<Hook>> deserializeAndReadHooks() {
        File file = new File("src/main/java/cz/meind/microomega/Database/Files/messages.dat");
        HashMap<ArrayList<User>, LinkedList<Hook>> map = new HashMap<>();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return map;
            }
        }
        Scanner scanner;
        try {
            scanner = new Scanner(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNext()) {
            String line = URLDecoder.decode(scanner.next(), StandardCharsets.UTF_8);
            User user1 = Database.userId(line.split("usr=")[1].split(",")[0]);
            User user2 = Database.userId(line.split("usr=")[1].split(",")[1].split("msg=")[0]);
            String[] messages = line.split("msg=")[1].split(",");
            ArrayList<User> users = new ArrayList<>();
            LinkedList<Hook> hooks = new LinkedList<>();
            users.add(user1);
            users.add(user2);
            for (String message : messages) {
                if (!Objects.equals(message, "")) {
                    try {
                        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(message));
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        SerializableMessage obj = (SerializableMessage) ois.readObject();
                        if (obj.sender.equals(user1)) {
                            hooks.add(new Hook(obj.text, obj.time, user1, user2));
                        } else if (obj.sender.equals(user2)) {
                            hooks.add(new Hook(obj.text, obj.time, user2, user1));
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            map.put(users, hooks);
        }
        scanner.close();
        return map;
    }

}
