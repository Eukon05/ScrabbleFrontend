package org.example.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

public class StatusController {
    @FXML
    private GridPane gridPane;

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
    }

    public boolean takeLetter(char letter) {
        letter = Character.toUpperCase(letter);
        for(int i = 0; i < currentLetters; i++) {
            if(playerLetters[i].getText().equals(String.valueOf(letter))) {
                for(int j = i + 1; j < currentLetters; j++)
                    playerLetters[j - 1].setText(playerLetters[j].getText());

                currentLetters--;
                playerLetters[currentLetters].setText("");
                playerLetters[currentLetters].setStyle("");

                return true;
            }
        }
        return false;
    }

    public void addLetter(char letter) {
        if(currentLetters < 7) {
            playerLetters[currentLetters].setText(String.valueOf(Character.toUpperCase(letter)));
            playerLetters[currentLetters].setStyle("-fx-border-color: white; -fx-text-fill: white; -fx-background-color: #328720;");
            currentLetters++;
        }
    }


}
