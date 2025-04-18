package org.example.chat;

import javafx.application.Platform;
import org.example.controller.ChatController;
import org.example.core.ClientConnectionDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatSession {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static ChatController cont;

    private ChatSession(){}

    public static void setChatController(ChatController cont) {
        ChatSession.cont = cont;
    }

    public static void sendMsg(String input) throws IOException {
        ChatSession.out.writeUTF(input);
        ChatSession.out.flush();
    }

    public static void login(String username, String roomID, String hostname, int port) throws IOException {
        Socket socket = new Socket(hostname, port);
        out = new ObjectOutputStream(socket.getOutputStream());

        ClientConnectionDTO conn = new ClientConnectionDTO(username, roomID);
        out.writeObject(conn);
        out.flush();

        new Thread(() -> {
            try {
                in = new ObjectInputStream(socket.getInputStream());

                while (true) {
                    ChatMessage msg = (ChatMessage) in.readObject();
                    System.out.println(msg);
                    Platform.runLater(() -> cont.updateLog(msg));
                }
            }
            catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
