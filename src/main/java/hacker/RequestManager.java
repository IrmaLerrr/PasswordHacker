package hacker;

import com.google.gson.Gson;
import model.AuthRequest;
import model.AuthResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RequestManager {
    private final DataInputStream input;
    private final DataOutputStream output;
    private final Gson gson = new Gson();

    public RequestManager(DataInputStream input, DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    public String sendSimpleRequest(String msgIn){
        try {
            output.writeUTF(msgIn);
            return input.readUTF();
        } catch (IOException e) {
            System.out.println("Error while executing request");
            return null;
        }
    }

    public String sendRequest(String login, String password){
        AuthRequest authRequest = new AuthRequest(login, password);
        String msgIn = gson.toJson(authRequest);
        try {
            output.writeUTF(msgIn);
            String msgOut = input.readUTF();
            AuthResponse authResponse =  gson.fromJson(msgOut, AuthResponse.class);
            return authResponse.result();

        } catch (IOException e) {
            System.out.println("Error while executing request");
            return null;
        }
    }
}
