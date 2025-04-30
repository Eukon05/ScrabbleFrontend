package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.chat.ChatSession;

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
    private TextField port;

    @FXML
    private Button loginBtn;

    @FXML
    protected void onLogin() {
        try {
            ChatSession session = ChatSession.login(username.getText(), roomID.getText(), hostname.getText(), Integer.parseInt(port.getText()));

            Stage stage = (Stage) loginBtn.getScene().getWindow();

            FXMLLoader appLoader = new FXMLLoader(getClass().getResource("app-view.fxml"));
            FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
            FXMLLoader dictionaryLoader = new FXMLLoader(getClass().getResource("dictionary-view.fxml"));

            Scene scene = new Scene(appLoader.load(), 600, 400);

            Node chatView = chatLoader.load();
            Node dictionaryView = dictionaryLoader.load();

            AppController controller = appLoader.getController();
            controller.initialize(chatView, dictionaryView);

            ChatController cont = chatLoader.getController();
            cont.setSession(session);

            stage.setOnCloseRequest(e -> System.exit(0));
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e) {
            connectionAlert.showAndWait();
        }
    }
}