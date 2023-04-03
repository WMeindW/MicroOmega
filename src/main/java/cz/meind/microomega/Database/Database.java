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
            return null;
        }
        return out.toString();
    }

    public static boolean serializeAndWrite(User user) {
        File file = new File("cz/meind/microomega/Database/Files/files.dat");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) return false;
            } catch (IOException e) {
                return false;
            }
        }
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file, true));
            stream.writeObject(user);
            stream.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static ArrayList<User> deserializeAndRead() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File("cz/meind/microomega/Database/Files/files.dat");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) return users;
            } catch (IOException e) {
                return users;
            }
        }
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(scanner.nextLine().getBytes(StandardCharsets.UTF_8)));
                User user = (User) stream.readObject();
                users.add(user);
            }
        } catch (IOException | ClassNotFoundException e) {
            return users;
        }
        return users;
    }

}
