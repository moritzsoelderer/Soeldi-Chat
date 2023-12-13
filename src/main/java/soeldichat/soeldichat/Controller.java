package soeldichat.soeldichat;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class Controller {

    private SoeldiChatApplication application;
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

    public void setApplication(SoeldiChatApplication application) {
        this.application = application;
    }

    public void displayChat(String currentContactNumber, String userNumber){
        application.setCurrentContactNumber(currentContactNumber);
        List<Message> messageList = application.loadMessages();
        //clearing chat
        messageContainer.getChildren().clear();

        //displaying Chat
        messageList.forEach(x -> addMessageToChat(x.getText(), x.getSender().equals(userNumber)));
    }

    public void displayContacts(List<Contact> contactList, String userNumber) {
        contactList.forEach(x -> {

            //add Container for contact
            HBox contactContainer = new HBox();
            contactContainer.maxHeight(50.0);
            contactContainer.getStyleClass().add("contact");

            //add ImageView for profile picture
            ImageView imageView = new ImageView();
            contactContainer.getChildren().add(imageView);
            HBox.setHgrow(imageView, Priority.NEVER);

            //add Label for name
            Label name = new Label(x.getFirstName() + " " + x.getLastName());
            contactContainer.getChildren().add(name);

            //display chat when clicked on contact
            String currentContactNumber = x.getNumber();
            contactContainer.setOnMouseClicked(y -> displayChat(currentContactNumber, userNumber));

            //add contact to contacts
            chatsContainer.getChildren().add(contactContainer);

        });
    }

    @FXML
    protected void onSendButtonClicked(){
        //return if message prompt is empty
        if(sendTextArea.getText().isEmpty()){return;}

        //add message to gui
        addMessageToChat(sendTextArea.getText(), true);

        //log message in json file
        application.logMessage(sendTextArea.getText());

        //clear message prompt
        sendTextArea.setText("");

        //scroll to message
        double vmax = currentChat.getVmax();
        currentChat.setVvalue(vmax);
    }

    @FXML
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