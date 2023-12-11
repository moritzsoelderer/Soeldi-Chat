module roborally.roborally {
    requires javafx.controls;
    requires javafx.fxml;


    opens roborally.roborally to javafx.fxml;
    exports roborally.roborally;
}