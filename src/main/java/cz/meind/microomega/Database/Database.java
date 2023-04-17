package cz.meind.microomega.Database;

import cz.meind.microomega.User.User;

import java.io.*;
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
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream stream = new ObjectOutputStream(os);
            stream.writeObject(user);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.append(Base64.getEncoder().encodeToString(os.toByteArray())).append('\n');
            writer.close();
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
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNext()) {
            InputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(scanner.next()));
            try {
                ObjectInputStream stream = new ObjectInputStream(in);
                users.add((User) stream.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return users;
    }

    public static String generate(User user) {
        HashMap<String, String> map = readIds();
        Random random = new Random();
        while (true) {
            String id = "SSNID-" + ((double) random.nextInt(1, 9999) / random.nextInt(1, 9999));
            if (!map.containsValue(id)) {
                map.remove(user.getUserName());
                map.put(user.getUserName(), id);
                if (writeIds(map)) {
                    return id;
                }
            }
        }
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
            writer = new FileWriter(file, true);
        } catch (IOException e) {
            return false;
        }
        for (int i = 0; i < idLogs.size(); i++) {
            try {
                writer.append(String.valueOf(idLogs.keySet().toArray()[i])).append(",").append(String.valueOf(idLogs.values().toArray()[i]));
                writer.close();
            } catch (IOException e) {
                return false;
            }
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


}
