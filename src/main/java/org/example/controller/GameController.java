package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class GameController {
    @FXML
    private HBox hBox;

    @FXML
    public void initialize(Node... nodes) {
        for(Node node : nodes) {
            hBox.getChildren().add(node);
        }
    }
}
