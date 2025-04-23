package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.chat.ChatSession;

import java.io.IOException;

public class HelloController {
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
    protected void onLogin() throws IOException {
        Stage stage = (Stage) loginBtn.getScene().getWindow();

        FXMLLoader appLoader = new FXMLLoader(getClass().getResource("app-view.fxml"));
        FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
        FXMLLoader dictionaryLoader = new FXMLLoader(getClass().getResource("dictionary-view.fxml"));

        Scene scene = new Scene(appLoader.load(), 600, 400);

        Node chatView = chatLoader.load();
        Node dictionaryView = dictionaryLoader.load();

        AppController controller = appLoader.getController();
        controller.initialize(chatView, dictionaryView);

        ChatSession session = ChatSession.login(username.getText(), roomID.getText(), hostname.getText(), Integer.parseInt(port.getText()));

        ChatController cont = chatLoader.getController();
        cont.setSession(session);

        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setScene(scene);
        stage.show();
    }
}