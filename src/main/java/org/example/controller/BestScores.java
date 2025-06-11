package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class BestScores {

    @FXML
    public void onClose(ActionEvent event) {
        // Zamknij bieżące okno (Stage) po kliknięciu przycisku "Close"
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

