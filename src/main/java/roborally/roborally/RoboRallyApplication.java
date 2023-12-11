package roborally.roborally;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RoboRallyApplication extends Application {

    private final String userNumber = "0000000000";
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoboRallyApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 900);
        ((Controller)fxmlLoader.getController()).loadChat(userNumber);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setMinWidth(650);
        stage.setMinHeight(400);
        stage.setTitle("SoeldiChat");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}