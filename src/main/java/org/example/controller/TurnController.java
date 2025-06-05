package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.core.ScrabbleSession;

import java.io.IOException;

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

    private ScrabbleSession session;

    public void initialize() {

        for(int i = 1; i < 16; i++){
            rowBox.getItems().add(i);
            colBox.getItems().add((char)('A' + i - 1));
        }

        anchorPane.setVisible(false);
    }

    public void setVisibility(boolean visible){
        anchorPane.setVisible(visible);
    }

    public void setSession(ScrabbleSession session) {
        this.session = session;
    }

    public void attemptPlace() throws IOException {
        session.attemptPlace(wordField.getText(), rowBox.getValue(), colBox.getValue(), vertCheck.isSelected());
    }

    public void skip() throws IOException {
        session.skip();
    }
}
