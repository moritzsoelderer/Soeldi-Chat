module soeldichat.soeldichat {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens soeldichat.soeldichat to javafx.fxml, com.google.gson;
    exports soeldichat.soeldichat;
}