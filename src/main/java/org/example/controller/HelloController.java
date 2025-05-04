package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.chat.ChatSession;
import org.example.dictionary.Dictionary;

public class HelloController {
    private final Alert connectionAlert = new Alert(Alert.AlertType.ERROR);

    public HelloController() {
        connectionAlert.setTitle("Error");
        connectionAlert.setHeaderText("Could not connect to the server!");
        connectionAlert.setContentText("Check the address and port and try again");
    }

    @FXML
    private TextField username;

    @FXML
    private TextField roomID;

    @FXML
    private TextField hostname;

    @FXML
    private Button loginBtn;

    @FXML
    protected void onLogin() {
        try {
            ChatSession session = ChatSession.login(username.getText(), roomID.getText(), hostname.getText(), 1122);
            Dictionary.setHostname(hostname.getText());

            Stage stage = (Stage) loginBtn.getScene().getWindow();

            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("board-view.fxml"));
            FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
            FXMLLoader statusLoader = new FXMLLoader(getClass().getResource("status-view.fxml"));
            FXMLLoader dictionaryLoader = new FXMLLoader(getClass().getResource("dictionary-view.fxml"));
            FXMLLoader turnLoader = new FXMLLoader(getClass().getResource("turn-view.fxml"));

            Scene scene = new Scene(gameLoader.load(), 1100, 595);

            VBox controls = new VBox();

            Node chatView = chatLoader.load();
            Node dictionaryView = dictionaryLoader.load();
            Node statusView = statusLoader.load();
            Node turnView = turnLoader.load();
            Node boardView = boardLoader.load();

            controls.getChildren().addAll(statusView, turnView, chatView, dictionaryView);

            GameController gameController = gameLoader.getController();
            gameController.initialize(boardView, controls);

            ChatController cont = chatLoader.getController();
            cont.setSession(session);

            BoardController boardController = boardLoader.getController();

            TurnController turnController = turnLoader.getController();
            turnController.initialize(boardController);

            stage.setOnCloseRequest(e -> System.exit(0));
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
            connectionAlert.showAndWait();
        }
    }
}