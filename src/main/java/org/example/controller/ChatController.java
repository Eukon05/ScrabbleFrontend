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
        String formattedMessage = "YOU: " + input.getText();
        chatLog.setText(chatLog.getText().concat(formattedMessage).concat("\n"));
        input.clear();
    }

    @FXML
    public void updateLog(ChatMessage message){
        String formattedMsg = message.nickname().concat(": ").concat(message.content());
        chatLog.setText(chatLog.getText().concat(formattedMsg).concat("\n"));
    }
}
