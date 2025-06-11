package org.example.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.example.core.ScrabbleSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.controller.BoardController.getLetterScore;

public class StatusController {
    @FXML
    private GridPane gridPane;

    @FXML
    private Label statusLabel;

    @FXML
    private Label playerLabel;

    @FXML
    private Button startButton;

    @FXML
    private Label scoresLabel;

    private ScrabbleSession session;
    private List<String> playerList = new ArrayList<>();

    private int currentLetters = 0;
    private final Label[] playerLetters = new Label[7];

    public void initialize() {
        for(int i = 0; i < 7; i++) {
            playerLetters[i] = new Label("");
            playerLetters[i].setAlignment(Pos.CENTER);
            playerLetters[i].setTextAlignment(TextAlignment.CENTER);
            playerLetters[i].setMaxWidth(Double.MAX_VALUE);
            playerLetters[i].setMaxHeight(Double.MAX_VALUE);

            gridPane.add(playerLetters[i], i, 0);
        }

        statusLabel.setText("Waiting for the game to start...");
    }

    public void clearLetters(){
        for(Label letter : playerLetters) {
            letter.setText("");
            letter.setStyle("");
        }
        currentLetters = 0;
    }

    public void addLetter(char letter) {
        if(currentLetters < 7) {
            playerLetters[currentLetters].setText(String.valueOf(Character.toUpperCase(letter)) + getLetterScore(letter));
            playerLetters[currentLetters].setStyle("-fx-border-color: white; -fx-text-fill: #FFFADC; -fx-background-color: #8C6A5D;");
            currentLetters++;
        }
    }

    public void hideStart(){
        startButton.setVisible(false);
    }

    public void startGame() throws IOException {
        session.startGame();
    }

    public void setStatus(String status) {
        setStatus(status, Color.BLACK);
    }

    public void setStatus(String status, Color color) {
        statusLabel.setText(status);
        statusLabel.setTextFill(color);
    }

    public void setScores(String scores) {
        /*String [] parts = scores.split("|");
        ArrayList<String> sortedScores = new ArrayList<>();
        for (int i=1; i < parts.length; i++) {
            sortedScores.add(parts[i]);
        }
        Collections.sort(sortedScores);
        String text = new String();
        for (String s : sortedScores) {
            text += s + " | ";
        }*/
        scoresLabel.setText(scores);
    }

    public void addPlayer(String player) {
        playerList.add(player);
        playerLabel.setText("Players: " + String.join(", ", playerList));
    }

    public void setSession(ScrabbleSession session) {
        this.session = session;
    }
}
