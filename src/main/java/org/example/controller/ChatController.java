package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.chat.ChatMessage;
import org.example.chat.ChatSession;

import java.io.IOException;

public class ChatController {
    private ChatSession session;

    @FXML
    private TextArea chatLog;
    @FXML
    private TextField input;

    public void setSession(ChatSession session) {
        session.setChatController(this);
        this.session = session;
    }

    @FXML
    public void sendMsg() throws IOException {
        session.sendMsg(input.getText());
        chatLog.setText(chatLog.getText().concat(input.getText()).concat("\n"));
        input.clear();
    }

    @FXML
    public void updateLog(ChatMessage message){
        chatLog.setText(chatLog.getText().concat(String.join(": ", message.nickname(), message.content())).concat("\n"));
    }
}
