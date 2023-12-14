package soeldichat.soeldichat;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    private VBox chatsContainer;
    @FXML
    private TextArea sendTextArea;
    @FXML
    private VBox messageContainer;

    public void setApplication(SoeldiChatApplication application) {
        this.application = application;
    }

    public void displayChat(List<Message> messageList){
        //clearing chat
        messageContainer.getChildren().clear();

        //displaying Chat
        messageList.forEach(message -> {
            HBox messageWrapper = createMessageHBox(message.getText(), message.getSender().equals(application.getUserNumber()));
            messageContainer.getChildren().add(messageWrapper);
        });
    }

    public void displayContacts(List<Contact> contactList) {
        contactList.forEach(contact -> {

            //create HBox and content for contact
            HBox contactContainer = createContactHBox(contact);

            //display chat when clicked on contact
            String currentContactNumber = contact.getNumber();
            contactContainer.setOnMouseClicked(y -> {
                application.setCurrentContactNumber(currentContactNumber);
                List<Message> messageList = Contact.getContactByNumber(contactList, currentContactNumber).getMessageList();
                displayChat(messageList);
            });

            //add contact to contacts
            chatsContainer.getChildren().add(contactContainer);

        });
    }
    @FXML
    protected void onSendButtonClicked(){
        //return if message prompt is empty
        if(sendTextArea.getText().isEmpty()){return;}

        //add message
        addMessageToChat(sendTextArea.getText(), true);

        //clear message prompt
        sendTextArea.setText("");

        //scroll to message
        double vmax = currentChat.getVmax();
        currentChat.setVvalue(vmax);
    }

    protected void addMessageToChat(String text, boolean alignRight){
        //Use HBox as Container to align message on the right
        HBox messageWrapper = createMessageHBox(text, alignRight);
        messageContainer.getChildren().add(messageWrapper);

        //update messageList
        application.addMessageToChat(new Message(application.getUserNumber(), application.getCurrentContactNumber(), text, ""));
    }

    private HBox createContactHBox(Contact contact){
        //add Container for contact
        HBox contactContainer = new HBox();
        contactContainer.maxHeight(50.0);
        contactContainer.getStyleClass().add("contact");

        //add ImageView for profile picture
        ImageView imageView = new ImageView();
        contactContainer.getChildren().add(imageView);
        HBox.setHgrow(imageView, Priority.NEVER);

        //add Label for name
        Label name = new Label(contact.getFirstName() + " " + contact.getLastName());
        contactContainer.getChildren().add(name);

        return contactContainer;
    }

    private HBox createMessageHBox(String text, boolean alignRight){
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

        return messageWrapper;
    }
}

