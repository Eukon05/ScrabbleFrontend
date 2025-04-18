package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.dictionary.Dictionary;

import java.io.IOException;

public class DictionaryController {
    @FXML
    private TextField input;

    @FXML
    private Label result;

    @FXML
    public void onCheckBtn() throws IOException {
        boolean res = Dictionary.checkWord(input.getText());
        result.setText(res ? "Exists!" : "Does not exist!");
    }
}
