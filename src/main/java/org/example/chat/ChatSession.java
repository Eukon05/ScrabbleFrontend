package org.example.chat;

import javafx.application.Platform;
import org.example.controller.ChatController;
import org.example.core.ClientConnectionDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatSession {
    private final ObjectOutputStream out;
    private ChatController cont;

    private ChatSession(Socket socket) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

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

    public void setChatController(ChatController cont) {
        this.cont = cont;
    }

    public void sendMsg(String input) throws IOException {
        out.writeUTF(input);
        out.flush();
    }

    public static ChatSession login(String username, String roomID, String hostname, int port) throws IOException {
        Socket socket = new Socket(hostname, port);
        ChatSession session = new ChatSession(socket);

        ClientConnectionDTO conn = new ClientConnectionDTO(username, roomID);
        session.out.writeObject(conn);
        session.out.flush();

        return session;
    }
}
