package org.example.core;

import javafx.application.Platform;
import org.example.controller.BoardController;
import org.example.controller.StatusController;
import org.example.controller.TurnController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ScrabbleSession {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final String username;

    private StatusController statusController;
    private BoardController boardController;
    private TurnController turnController;

    private ScrabbleSession(String username, Socket socket) throws IOException {
        this.socket = socket;
        this.username = username;
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void setStatusController(StatusController statusController) {
        this.statusController = statusController;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setTurnController(TurnController turnController) {
        turnController.setSession(this);
        this.turnController = turnController;
    }

    public void start() {
        new Thread(() -> {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                while (!socket.isClosed()) {
                    String response = (String) in.readObject();
                    System.out.println(response);

                    if(response.equals("TURN " + username)) {
                        Platform.runLater(() -> turnController.setVisibility(true));
                    }
                    else if(response.startsWith("RACK")){
                        Platform.runLater(() -> statusController.clearLetters());

                        String rack = response.split(" ")[1];
                        for(char letter : rack.toCharArray()){
                            Platform.runLater(() -> statusController.addLetter(letter));
                        }
                    }
                    else if(response.equals("INVALID")) {
                        Platform.runLater(() -> turnController.setVisibility(true));

                    }
                    else if(response.startsWith("PLACE")) {
                        String[] move = response.split(" ");
                        Platform.runLater(() -> boardController.placeWord(move[1], Integer.parseInt(move[3]), move[2].charAt(0), move[4].charAt(0) == 'V'));
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("Disconnected from server.");
            }
            catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void attemptPlace(String word, int row, char col, boolean vertical) throws IOException {
        Platform.runLater(() -> turnController.setVisibility(false));

        String move = "PLACE %s %s %d %s".formatted(word, col, row, vertical ? 'V' : 'H');
        out.writeObject(move);
        out.flush();
    }

    public void skip() throws IOException {
        Platform.runLater(() -> turnController.setVisibility(false));
        out.writeObject("SKIP");
        out.flush();
    }

    public static ScrabbleSession login(String username, String roomID, String hostname, int port) throws IOException {
        Socket socket = new Socket(hostname, port);
        ScrabbleSession game = new ScrabbleSession(username, socket);

        game.out.writeObject(new ClientConnectionDTO(username, roomID));
        game.out.flush();

        return game;
    }
}
