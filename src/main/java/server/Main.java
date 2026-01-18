package server;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int SERVER_PORT = 23456;
    private static final char[] allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    private static final Random random = new Random();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started!");
            String login = generateDictionaryBasedLogin();
            String pass = generatePass(random.nextInt(10) + 5);
            System.out.println("Correct login: " + login);
            System.out.println("Correct password: " + pass);
            while (true) {
                try (Socket clientSocket = server.accept()) {
                    System.out.println("Client connected!");

                    DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
                    RequestManager requestManager = new RequestManager(input, output);

                    while (true) {
                        requestManager.acceptRequest(login, pass);
                    }

                } catch (EOFException e2) {
                    System.out.println("The client disconnected.");
                } catch (SocketException e3) {
                    System.out.println("The connection was broken.");
                } catch (IOException | InterruptedException e) {
                    System.out.println("Error! " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error! " + e.getMessage());
        }
    }

    private static String generatePass(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(allChars[random.nextInt(allChars.length)]);
        }

        return password.toString();
    }

    @Deprecated
    private static String generateDictionaryBasedPass() {
        return diversifyRegister(FileManager.getPassList());
    }

    private static String generateDictionaryBasedLogin() {
        return diversifyRegister(FileManager.getLoginList());
    }

    private static String diversifyRegister(List<String> list) {
        String word = list.get(random.nextInt(list.size()));
        word = word.chars()
                .mapToObj(c -> (char) c)
                .map(c -> random.nextBoolean() ? Character.toUpperCase(c) : Character.toLowerCase(c))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        return word;
    }
}
