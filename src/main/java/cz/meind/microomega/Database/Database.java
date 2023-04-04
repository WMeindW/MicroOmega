package cz.meind.microomega.Database;

import cz.meind.microomega.User.User;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

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
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            ArrayList<User> users = deserializeAndRead();
            users.add(user);
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(users);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
            users = (ArrayList<User>) stream.readObject();
            stream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return users;
        }
        return users;
    }

}
