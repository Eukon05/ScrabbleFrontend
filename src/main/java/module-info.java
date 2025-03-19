module group.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens group.demo to javafx.fxml;
    exports group.demo;
}