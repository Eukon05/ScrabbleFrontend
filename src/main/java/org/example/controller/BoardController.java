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
    private static final String[] LAYOUT = {
            "3W,N,N,2L,N,N,N,3W,N,N,N,2L,N,N,3W",
            "N,2W,N,N,N,3L,N,N,N,3L,N,N,N,2W,N",
            "N,N,2W,N,N,N,2L,N,2L,N,N,N,2W,N,N",
            "2L,N,N,2W,N,N,N,2L,N,N,N,2W,N,N,2L",
            "N,N,N,N,2W,N,N,N,N,N,2W,N,N,N,N",
            "N,3L,N,N,N,3L,N,N,N,3L,N,N,N,3L,N",
            "N,N,2L,N,N,N,2L,N,2L,N,N,N,2L,N,N",
            "3W,N,N,2L,N,N,N,*,N,N,N,2L,N,N,3W",
    };

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

        // Prepare tile types
        String[] row;
        for (int i = 1; i < 16; i++) {
            row = (i <= 8) ? LAYOUT[i - 1].split(",") : LAYOUT[15 - i].split(",");
            for (int j = 1; j < 16; j++) {
                switch (row[j - 1]) {
                    case "*":
                        board[i][j].setText("*");
                        board[i][j].setStyle("-fx-border-color: black; -fx-background-color: #D95F59");
                        break;
                    case "2L":
                        board[i][j].setText("2L");
                        board[i][j].setStyle("-fx-border-color: black; -fx-background-color: #8cc542");
                        break;
                    case "3L":
                        board[i][j].setText("3L");
                        board[i][j].setStyle("-fx-border-color: black; -fx-background-color: #1675b7");
                        break;
                    case "2W":
                        board[i][j].setText("2W");
                        board[i][j].setStyle("-fx-border-color: black; -fx-background-color: #fb8f22");
                        break;
                    case "3W":
                        board[i][j].setText("3W");
                        board[i][j].setStyle("-fx-border-color: black; -fx-background-color: #bc202d");
                        break;
                    default: // N
                        board[i][j].setStyle("-fx-border-color: black; -fx-background-color: #EEE4B1");
                        break;
                }
            }
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
        board[row][col].setText(String.valueOf(letter).toUpperCase()+getLetterScore(letter));
        board[row][col].setStyle("-fx-border-color: white; -fx-text-fill: #FFFADC; -fx-background-color: #8C6A5D;");
    }

    public static String getLetterScore(char letter) {
        letter = Character.toUpperCase(letter);
        switch (letter) {
            case '-':
                return "";
            case 'E':
            case 'A':
            case 'I':
            case 'O':
            case 'N':
            case 'R':
            case 'T':
            case 'L':
            case 'S':
            case 'U':
                return " | 1";
            case 'D':
            case 'G':
                return " | 2";
            case 'B':
            case 'C':
            case 'M':
            case 'P':
                return " | 3";
            case 'F':
            case 'H':
            case 'V':
            case 'W':
            case 'Y':
                return " | 4";
            case 'K':
                return " | 5";
            case 'J':
            case 'X':
                return " | 8";
            case 'Q':
            case 'Z':
                return " | 10";
            default:
                return "";
        }
    }
}
