package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.dictionary.Dictionary;

import java.io.IOException;


public class DictionaryController {
    private static final String GREEN = "#94f288";
    private static final String RED = "#f28888";

    @FXML
    private TextField input;

    @FXML
    public void onCheckBtn() throws IOException {
        boolean res = Dictionary.checkWord(input.getText());
        input.setStyle("-fx-background-color: " + (res ? GREEN : RED) + ";");
    }
}
