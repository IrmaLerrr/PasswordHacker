package server;

import java.io.*;
import java.net.*;
import java.util.Random;

public class Main {
    private static final int SERVER_PORT = 23456;
    private static final char[] allChars = "abcdefghijklmnopqrstuvwxyz01234567890".toCharArray();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started!");
            String pass = generateSimplePass();
            System.out.println("Correct password: " + pass);
            while (true) {
                try {
                    Socket clientSocket = server.accept();
                    DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

                    while (true) {
                        String msgIn = input.readUTF();
                        if (msgIn.equals(pass)) output.writeUTF("Connection success!");
                        else output.writeUTF("Wrong password!");
                    }
//                    RequestManager manager = new RequestManager(input, output);
//                    manager.start();
                } catch (EOFException e2) {
                    System.out.println("The client disconnected.");
                } catch (SocketException e3) {
                    System.out.println("The connection was broken.");
                } catch (IOException e) {
                    System.out.println("Error! " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error! " + e.getMessage());
        }
    }

    private static String generateSimplePass(){
        Random random = new Random();
        StringBuilder password = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {
            password.append(allChars[random.nextInt(allChars.length)]);
        }

        return password.toString();
    }
}
