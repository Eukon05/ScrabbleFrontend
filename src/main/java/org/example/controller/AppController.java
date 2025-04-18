package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class AppController {
    @FXML
    private VBox layout;

    @FXML
    public void initialize(Node... nodes) {
        for(Node node : nodes) {
            layout.getChildren().add(node);
        }
    }
}
