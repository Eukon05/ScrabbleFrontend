package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.view.ScoresView;

import java.io.IOException;

public class ScoresController {
    private String hostame;

    public void initialize(String hostame) {
        this.hostame = hostame;
    }

    @FXML
    public void onOpenModal() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/view/BestScores.fxml"));
        Parent modalRoot = fxmlLoader.load();
        ScoresView controller = fxmlLoader.getController();
        controller.initialize(hostame);

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("High-scores");
        modalStage.setScene(new Scene(modalRoot));
        modalStage.showAndWait();
    }
}
