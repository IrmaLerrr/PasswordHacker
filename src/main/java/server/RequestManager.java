package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.AuthRequest;
import model.AuthResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class RequestManager {
    private final DataInputStream input;
    private final DataOutputStream output;
    private final Gson gson = new Gson();

    public RequestManager(DataInputStream input, DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    @Deprecated
    public void acceptSimpleRequest(String pass) throws IOException {
        String msgIn = input.readUTF();
        if (msgIn.equals(pass)) output.writeUTF("Connection success!");
        else output.writeUTF("Wrong password!");
    }

    public void acceptRequest(String corrrectLogin, String correctPassword) throws IOException {
        String msgOut = input.readUTF();
        AuthRequest authRequest;
        try {
            authRequest = gson.fromJson(msgOut, AuthRequest.class);
        } catch (JsonSyntaxException e) {
            output.writeUTF(gson.toJson(new AuthResponse("Bad request!")));
            return;
        }
        String login = authRequest.login();
        String password = authRequest.password();

        if (!Objects.equals(corrrectLogin, login)) {
            output.writeUTF(gson.toJson(new AuthResponse("Wrong login!")));
        } else if (Objects.equals(correctPassword, password)) {
            output.writeUTF(gson.toJson(new AuthResponse("Connection success!")));
        } else if (correctPassword.startsWith(password)) {
            output.writeUTF(gson.toJson(new AuthResponse("Exception happened during login")));
        } else {
            output.writeUTF(gson.toJson(new AuthResponse("Wrong password!")));
        }
    }
}
