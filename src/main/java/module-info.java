open module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.logging;


    exports org.example;
    exports org.example.controller;
    exports org.example.dictionary;
    exports org.example.chat;
}