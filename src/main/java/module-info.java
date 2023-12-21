module soeldichat.soeldichat {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    requires static lombok;


    opens soeldichat.soeldichat to javafx.fxml, com.google.gson;
    exports soeldichat.soeldichat;
}