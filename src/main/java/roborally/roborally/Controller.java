package roborally.roborally;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class Controller {
    @FXML
    private ScrollPane currentChat;
    @FXML
    private HBox sendContainer;
    @FXML
    private VBox chatsContainer;
    @FXML
    private VBox currentChatContainer;
    @FXML
    private TextArea sendTextArea;
    @FXML
    private Button sendButton;
    @FXML
    private VBox messageContainer;
    @FXML
    private HBox root;

    public void loadChat(String userNumber) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("C:\\Users\\morit\\IdeaProjects\\RoboRally\\src\\main\\resources\\roborally\\roborally\\chat.txt"));
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.substring(0,10).equals(userNumber)){
                addMessageToChat(line.substring(11), true);
            }
            else{
                addMessageToChat(line.substring(11), false);
            }
        }
    }


    @FXML
    protected void onSendButtonClicked(){
        //return if message prompt is empty
        if(sendTextArea.getText().isEmpty()){return;}
        addMessageToChat(sendTextArea.getText(), true);

        //clear message prompt
        sendTextArea.setText("");

        //scroll to message
        double vmax = currentChat.getVmax();
        currentChat.setVvalue(vmax);
    }

    protected void addMessageToChat(String text, boolean alignRight){
        //Use HBox as Container to align message on the right
        HBox messageWrapper = new HBox();

        Label message = new Label();
        message.setWrapText(true);

        message.setText(text);
        messageWrapper.getChildren().add(message);
        if(alignRight){
            message.getStyleClass().add("sentMessages");
            messageWrapper.setAlignment(Pos.BASELINE_RIGHT);
        }
        else{
            message.getStyleClass().add("recievedMessages");
            messageWrapper.setAlignment(Pos.BASELINE_LEFT);
        }
        messageContainer.getChildren().add(messageWrapper);
    }
}