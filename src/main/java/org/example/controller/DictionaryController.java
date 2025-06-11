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
    @FXML
    private TextField input;

    @FXML
    private Label result;

    @FXML
    public void onCheckBtn() throws IOException {
        boolean res = Dictionary.checkWord(input.getText());
        result.setText(res ? "Exists!" : "Does not exist!");
    }

    @FXML
    public void onOpenModal() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/view/BestScores.fxml"));
        Parent modalRoot = fxmlLoader.load();

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("About");
        modalStage.setScene(new Scene(modalRoot));
        modalStage.showAndWait();
    }

}
