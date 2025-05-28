package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class TurnController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ChoiceBox<Character> colBox;

    @FXML
    private ChoiceBox<Integer> rowBox;

    @FXML
    private TextField wordField;

    @FXML
    private CheckBox vertCheck;

    private BoardController boardController;

    public void initialize(BoardController boardController) {
        this.boardController = boardController;

        for(int i = 1; i < 16; i++){
            rowBox.getItems().add(i);
            colBox.getItems().add((char)('A' + i - 1));
        }
    }

    public void toggleVisibility(){
        anchorPane.setVisible(!anchorPane.isVisible());
    }

    public void attemptPlace(){
        boardController.placeWord(wordField.getText(), rowBox.getValue(), colBox.getValue(), vertCheck.isSelected());
        toggleVisibility();
    }
}
