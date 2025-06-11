package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.chat.ChatSession;
import org.example.core.ScrabbleSession;
import org.example.dictionary.Dictionary;
import org.example.user.UserManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloController {
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final Alert connectionAlert = new Alert(Alert.AlertType.ERROR);
    private final Alert loginAlert = new Alert(Alert.AlertType.ERROR);

    public HelloController() {
        connectionAlert.setTitle("Error");
        connectionAlert.setHeaderText("Could not connect to the server!");
        connectionAlert.setContentText("Check the address and port and try again");

        loginAlert.setTitle("Error");
        loginAlert.setHeaderText("Invalid login data!");
        loginAlert.setContentText("Check your username and password and try again");
    }

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private TextField roomID;

    @FXML
    private TextField hostname;

    @FXML
    private Button loginBtn;

    @FXML
    protected void onLogin() {
        try {
            int loginResponse = UserManager.tryLogin(username.getText(), password.getText(), hostname.getText());

            if(loginResponse == 400) {
                throw new IllegalArgumentException();
            }
            else if(loginResponse == 401) {
                int registerResponse = UserManager.tryRegister(username.getText(), password.getText(), hostname.getText());
                if(registerResponse != 200) {
                    throw new IllegalArgumentException();
                }
            }

            ChatSession chatSession = ChatSession.login(username.getText(), roomID.getText(), hostname.getText(), 1122);
            Dictionary.setHostname(hostname.getText());

            Stage stage = (Stage) loginBtn.getScene().getWindow();

            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("board-view.fxml"));
            FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
            FXMLLoader statusLoader = new FXMLLoader(getClass().getResource("status-view.fxml"));
            FXMLLoader dictionaryLoader = new FXMLLoader(getClass().getResource("dictionary-view.fxml"));
            FXMLLoader turnLoader = new FXMLLoader(getClass().getResource("turn-view.fxml"));
            FXMLLoader scoresLoader = new FXMLLoader(getClass().getResource("scores-view.fxml"));

            Scene scene = new Scene(gameLoader.load(), 1100, 592);

            VBox controls = new VBox();

            Node chatView = chatLoader.load();
            Node dictionaryView = dictionaryLoader.load();
            Node statusView = statusLoader.load();
            Node turnView = turnLoader.load();
            Node boardView = boardLoader.load();
            Node scoresView = scoresLoader.load();

            controls.getChildren().addAll(statusView, turnView, chatView, dictionaryView, scoresView);

            GameController gameController = gameLoader.getController();
            gameController.initialize(boardView, controls);

            ChatController cont = chatLoader.getController();
            cont.setSession(chatSession);

            BoardController boardController = boardLoader.getController();

            TurnController turnController = turnLoader.getController();
            StatusController statusController = statusLoader.getController();

            ScoresController scoresController = scoresLoader.getController();
            scoresController.initialize(hostname.getText());

            ScrabbleSession scrabbleSession = ScrabbleSession.login(username.getText(), roomID.getText(), hostname.getText(), 1123);
            scrabbleSession.setTurnController(turnController);
            scrabbleSession.setStatusController(statusController);
            scrabbleSession.setBoardController(boardController);

            executor.submit(chatSession);
            executor.submit(scrabbleSession);

            stage.setOnCloseRequest(e -> System.exit(0));
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
        catch (IllegalArgumentException e){
            loginAlert.showAndWait();
        }
        catch(Exception e) {
            e.printStackTrace();
            connectionAlert.showAndWait();
        }
    }
}