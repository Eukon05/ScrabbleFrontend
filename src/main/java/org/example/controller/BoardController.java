package org.example.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

public class BoardController {
    @FXML
    private GridPane gridPane;

    private final Label[][] board = new Label[16][16];

    public void initialize() {
        for(int row = 0; row < 16; row++) {
            for(int col = 0; col < 16; col++) {
                Label tile = new Label("");
                tile.setMaxWidth(Double.MAX_VALUE);
                tile.setMaxHeight(Double.MAX_VALUE);
                tile.setAlignment(Pos.CENTER);
                tile.setTextAlignment(TextAlignment.CENTER);
                gridPane.add(tile, col, row);
                board[row][col] = tile;
            }
        }

        // Prepare coordinate tiles
        for(int i = 1; i < 16; i++){
            board[0][i].setText(String.valueOf((char)('A' + i - 1)));
            board[i][0].setText(String.valueOf(i));
            board[0][i].setStyle("-fx-background-color: #d8e0ed");
            board[i][0].setStyle("-fx-background-color: #d8e0ed");
        }
    }

    public boolean placeWord(String word, int row, char col, boolean vertical){
        col = Character.toUpperCase(col);
        return placeWord(word, row, col - 'A' + 1, vertical);
    }

    public boolean placeWord(String word, int row, int col, boolean vertical) {
        if(row < 1 || row > 15 || col < 1 || col > 15)
            return false;

        if(vertical && word.length() + row - 1 < 16){
            for(int i = 0; i < word.length(); i++)
                placeLetter(word.charAt(i), row + i, col);

            return true;
        }
        else if(word.length() + col - 1 < 16) {
            for(int i = 0; i < word.length(); i++)
                placeLetter(word.charAt(i), row, col + i);

            return true;
        }
        else
            return false;
    }

    private void placeLetter(char letter, int row, int col) {
        board[row][col].setText(String.valueOf(letter));
        board[row][col].setStyle("-fx-background-color: #e1e6d3");
    }
}
