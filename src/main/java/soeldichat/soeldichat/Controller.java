package soeldichat.soeldichat;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class Controller {

    private SoeldiChatApplication application;
    @FXML
    private ScrollPane currentChat;
    @FXML
    private VBox contactContainer;
    @FXML
    private TextArea sendTextArea;
    @FXML
    private VBox messageContainer;

    private HBox focusedContactContainer;

    public void setApplication(SoeldiChatApplication application) {
        this.application = application;
    }

    public void displayChat(List<Message> messageList){
        //clearing chat
        messageContainer.getChildren().clear();

        //displaying Chat
        messageList.forEach(message -> {
            HBox messageWrapper = createMessageHBox(message.getText(),message.getTimeStamp(), message.getSender().equals(application.getUserNumber()));
            messageContainer.getChildren().add(messageWrapper);
        });
    }

    public void displayContacts(List<Contact> contactList) {
        contactList.forEach(contact -> {

            //create HBox and content for contact
            HBox newContactContainer = createContactHBox(contact);

            //display chat when clicked on contact
            String focusedContactNumber = contact.getNumber();
            newContactContainer.setOnMouseClicked(y -> {
                //store number of focused contact
                application.setFocusedContactNumber(focusedContactNumber);
                //store Hbox of focused contact
                focusedContactContainer.getStyleClass().clear();
                focusedContactContainer.getStyleClass().add("contact");
                focusedContactContainer = newContactContainer;
                focusedContactContainer.getStyleClass().clear();
                focusedContactContainer.getStyleClass().add("focusedContact");
                List<Message> messageList = Contact.getContactByNumber(contactList, focusedContactNumber).getMessageList();
                displayChat(messageList);
            });
            //add contact to contacts
            this.contactContainer.getChildren().add(newContactContainer);

        });
        //focus first contact
        focusedContactContainer = (HBox) contactContainer.getChildren().get(1);
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
        String currentDateTime = java.time.LocalDateTime.now().toString();
        //Use HBox as Container to align message on the right
        HBox messageWrapper = createMessageHBox(text,currentDateTime, alignRight);
        messageContainer.getChildren().add(messageWrapper);

        //update messageList
        application.addMessageToChat(new Message(application.getUserNumber(), application.getFocusedContactNumber(), text, "", currentDateTime));

        //update last message
        ((Label)((VBox)focusedContactContainer.getChildren().get(1)).getChildren().getLast()).setText(text);
        contactContainer.getChildren().remove(focusedContactContainer);
        contactContainer.getChildren().add(1,focusedContactContainer);
    }

    private HBox createContactHBox(Contact contact){
        //add Container for contact
        HBox contactContainer = new HBox();
        contactContainer.getStyleClass().add("contact");

        //add ImageView for profile picture
        StackPane imageViewStackPane = new StackPane();
        ImageView imageView = new ImageView();
        if(contact.getProfilePicture().isEmpty()){
            //default profile picture
            imageView.setImage(new Image("file:C:\\Users\\morit\\IdeaProjects\\RoboRally\\src\\main\\resources\\soeldichat\\soeldichat\\img\\profile-pictures\\boxer.jpg"));
        }
        else{
            try{imageView.setImage(new Image("file:C:\\Users\\morit\\IdeaProjects\\RoboRally\\src\\main\\resources\\soeldichat\\soeldichat\\img\\profile-pictures\\" +contact.getProfilePicture()));}
            catch(Exception exception){
                //default profile picture
                imageView.setImage(new Image("file:C:\\Users\\morit\\IdeaProjects\\RoboRally\\src\\main\\resources\\soeldichat\\soeldichat\\img\\profile-pictures\\0000000001.jpg"));
            }
        }

        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        imageViewStackPane.getStyleClass().add("contactProfilePicture");
        imageViewStackPane.getChildren().add(imageView);

        contactContainer.getChildren().add(imageViewStackPane);
        //HBox.setHgrow(imageView, Priority.NEVER);

        //add VBox for name and lastMessageText
        VBox nameStatusVbox = new VBox();
        contactContainer.getChildren().add(nameStatusVbox);

        //add Label for name
        Label name = new Label(contact.getFirstName() + " " + contact.getLastName());
        name.getStyleClass().add("contactName");
        nameStatusVbox.getChildren().add(name);

        //add label for lastMessageText
        Label lastMessageText = new Label(contact.getStatus());

        try{lastMessageText.setText(contact.getMessageList().getLast().getText());}
        catch(Exception ignored){}

        lastMessageText.getStyleClass().add("contactStatus");
        nameStatusVbox.getChildren().add(lastMessageText);

        return contactContainer;
    }

    private HBox createMessageHBox(String text, String timeStamp, boolean alignRight){
        HBox messageWrapper = new HBox();
        HBox messageHBox = new HBox();
        messageWrapper.setSpacing(3.0);

        //add timestamp
        Label timeStampLabel = new Label(timeStamp.substring(11,16));
        timeStampLabel.getStyleClass().add("timeStamp");

        //add text
        Label textLabel = new Label(text);
        textLabel.setWrapText(true);

        if(alignRight){
            textLabel.getStyleClass().add("sentMessages");
            messageWrapper.setAlignment(Pos.CENTER_RIGHT);

            messageHBox.getChildren().add(textLabel);
            messageHBox.getChildren().add(timeStampLabel);
            messageHBox.setAlignment(Pos.BOTTOM_LEFT);
            messageHBox.setSpacing(4.0);
        }
        else{
            textLabel.getStyleClass().add("recievedMessages");
            messageWrapper.setAlignment(Pos.CENTER_LEFT);

            messageHBox.getChildren().add(timeStampLabel);
            messageHBox.getChildren().add(textLabel);
            messageHBox.setAlignment(Pos.BOTTOM_RIGHT);
        }

        messageWrapper.getChildren().add(messageHBox);

        return messageWrapper;
    }
}

