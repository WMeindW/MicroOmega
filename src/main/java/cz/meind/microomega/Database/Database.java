package cz.meind.microomega.Database;

import java.io.File;
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
}
