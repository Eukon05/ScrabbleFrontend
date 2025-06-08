package org.example.core;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.example.controller.BoardController;
import org.example.controller.StatusController;
import org.example.controller.TurnController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScrabbleSession implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(ScrabbleSession.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("game.log");
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
        statusController.setSession(this);
        statusController.addPlayer(username);
        this.statusController = statusController;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setTurnController(TurnController turnController) {
        turnController.setSession(this);
        this.turnController = turnController;
    }

    public void attemptPlace(String word, int row, char col, boolean vertical) throws IOException {
        Platform.runLater(() -> turnController.setVisibility(false));

        String move = "PLACE %s %s %d %s".formatted(word, col, row, vertical ? 'V' : 'H');
        out.writeObject(move);
        out.flush();

        LOGGER.fine("Attempting to perform move: %s".formatted(move));
    }

    public void skip() throws IOException {
        Platform.runLater(() -> turnController.setVisibility(false));
        out.writeObject("SKIP");
        out.flush();

        LOGGER.fine("Skipping turn");
    }

    public void startGame() throws IOException {
        Platform.runLater(() -> statusController.hideStart());
        out.writeObject("SCRABBLE");
        out.flush();

        LOGGER.fine("Starting game");
    }

    public static ScrabbleSession login(String username, String roomID, String hostname, int port) throws IOException {
        Socket socket = new Socket(hostname, port);
        ScrabbleSession game = new ScrabbleSession(username, socket);

        game.out.writeObject(new ClientConnectionDTO(username, roomID));
        game.out.flush();

        LOGGER.info("Connected to %s:%d/%s".formatted(hostname, port, roomID));
        return game;
    }

    @Override
    public void run() {
        try(ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            while (!socket.isClosed()) {
                String response = (String) in.readObject();
                LOGGER.fine(response);

                if(response.equals("START")){
                    Platform.runLater(() -> statusController.hideStart());
                }
                else if(response.startsWith("TURN")) {
                    Platform.runLater(() -> {
                        statusController.setStatus("%s's turn".formatted(response.split(" ")[1]));

                        if(response.contains(username))
                            turnController.setVisibility(true);
                    });
                }
                else if(response.startsWith("RACK")){
                    Platform.runLater(() -> statusController.clearLetters());

                    String rack = response.split(" ")[1];
                    for(char letter : rack.toCharArray()){
                        Platform.runLater(() -> statusController.addLetter(letter));
                    }
                }
                else if(response.equals("INVALID")) {
                    Platform.runLater(() -> {
                        statusController.setStatus("Invalid move! Try again!", Color.RED);
                        turnController.setVisibility(true);
                    });

                }
                else if(response.startsWith("PLACE")) {
                    String[] move = response.split(" ");
                    Platform.runLater(() -> boardController.placeWord(move[1], Integer.parseInt(move[3]), move[2].charAt(0), move[4].charAt(0) == 'V'));
                }
                else if (response.startsWith("PLAYERS")) {
                    String[] players = response.split(" ");
                    for(int i = 1; i < players.length; i++){
                        String nick = players[i];
                        Platform.runLater(() -> statusController.addPlayer(nick));
                    }
                }
                else if (response.startsWith("JOIN")) {
                    Platform.runLater(() -> statusController.addPlayer(response.split(" ")[1]));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("Disconnected from server.");
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
