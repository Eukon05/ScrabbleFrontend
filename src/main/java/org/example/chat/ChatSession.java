package org.example.chat;

import javafx.application.Platform;
import org.example.controller.ChatController;
import org.example.core.ClientConnectionDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatSession implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ChatSession.class.getName());
    private final Socket socket;
    private final ObjectOutputStream out;

    private ChatController cont;

    static {
        try {
            FileHandler fileHandler = new FileHandler("chat.log");
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ChatSession(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void setChatController(ChatController cont) {
        this.cont = cont;
    }

    public void sendMsg(String input) throws IOException {
        out.writeUTF(input);
        out.flush();

        LOGGER.fine("Sending message: %s".formatted(input));
    }

    public static ChatSession login(String username, String roomID, String hostname, int port) throws IOException {
        Socket socket = new Socket(hostname, port);
        ChatSession session = new ChatSession(socket);

        ClientConnectionDTO conn = new ClientConnectionDTO(username, roomID);
        session.out.writeObject(conn);
        session.out.flush();

        LOGGER.info("Connected to %s:%d/%s".formatted(hostname, port, roomID));
        return session;
    }

    @Override
    public void run() {
        try(ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            while (!socket.isClosed()) {
                ChatMessage msg = (ChatMessage) in.readObject();
                LOGGER.fine(msg.toString());
                Platform.runLater(() -> cont.updateLog(msg));
            }
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
