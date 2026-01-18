package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FILE_PATH_PASS = "./src/main/resources/passwords.txt";
    private static final String FILE_PATH_LOGIN = "./src/main/resources/logins.txt";

    public static List<String> getPassList(){
        return readFile(FILE_PATH_PASS);
    }
    public static List<String> getLoginList(){
        return readFile(FILE_PATH_LOGIN);
    }
    public static List<String> readFile(String path){
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            List<String> list = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                list.add(line);
            }

            return list;
        } catch (IOException e) {
            System.out.println("Error! " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
