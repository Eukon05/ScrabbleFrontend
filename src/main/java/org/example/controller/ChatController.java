package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.chat.ChatMessage;
import org.example.chat.ChatSession;

import java.io.IOException;

public class ChatController {
    @FXML
    private TextArea chatLog;
    @FXML
    private TextField input;

    @FXML
    public void sendMsg() throws IOException {
        ChatSession.sendMsg(input.getText());
        chatLog.setText(chatLog.getText().concat(input.getText()).concat("\n"));
        input.clear();
    }

    @FXML
    public void updateLog(ChatMessage message){
        chatLog.setText(chatLog.getText().concat(message.toString()).concat("\n"));
    }
}
