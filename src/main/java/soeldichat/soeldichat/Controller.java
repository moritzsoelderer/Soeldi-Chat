package soeldichat.soeldichat;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.List;

public class Controller{
    private SoeldiChatApplication application;
    @FXML
    private ImageView chatMenuBarProfilePicture;
    @FXML
    private Label chatMenuBarContactName;
    @FXML
    private Label chatMenuBarStatus;
    @FXML
    private ScrollPane currentChat;
    @FXML
    private VBox contactVBox;
    @FXML
    private VBox contactScrollpaneVBox;
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
            HBox messageWrapper = createMessageHBox(message, message.getSender().equals(application.getUserNumber()));
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

                //update chatmenubar
                updateChatMenuBar();
            });
            //add contact to contacts
            contactScrollpaneVBox.getChildren().add(newContactContainer);

        });
        //focus first contact
        focusedContactContainer = (HBox) contactScrollpaneVBox.getChildren().getFirst();
    }

    public void updateChatMenuBar() {
        Contact focusedContact = Contact.getContactByNumber(application.getContactList(), application.getFocusedContactNumber());
        chatMenuBarContactName.setText(focusedContact.getFirstName() + " " + focusedContact.getLastName());
        chatMenuBarStatus.setText(focusedContact.getStatus());

        if(focusedContact.getProfilePicture().isEmpty()){
            //default profile picture
            chatMenuBarProfilePicture.setImage(application.loadImage(application.getDefaultProfilePictureString()));
        }
        else{
            try{chatMenuBarProfilePicture.setImage(application.loadImage(focusedContact.getProfilePicture()));}
            catch(Exception exception){
                //default profile picture
                chatMenuBarProfilePicture.setImage(application.loadImage(application.getDefaultProfilePictureString()));
            }
        }
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
        HBox messageWrapper = createMessageHBox(new Message(text, currentDateTime), alignRight);
        messageContainer.getChildren().add(messageWrapper);

        //update messageList
        application.addMessageToChat(new Message(application.getUserNumber(), application.getFocusedContactNumber(), text, "", currentDateTime));

        //update last message
        ((Label)((VBox)focusedContactContainer.getChildren().get(1)).getChildren().getLast()).setText(text);
        contactScrollpaneVBox.getChildren().remove(focusedContactContainer);
        contactScrollpaneVBox.getChildren().addFirst(focusedContactContainer);

        //update contact list and log contacts
        application.updateContactList(application.getFocusedContactNumber());
        application.logContacts();
    }

    private HBox createContactHBox(Contact contact){
        //add Container for contact
        HBox contactContainer = new HBox();
        contactContainer.getStyleClass().add("contact");

        //add ImageView for profile picture
        ImageView imageView = createImageView(contact);


        contactContainer.getChildren().add(imageView);

        //add VBox for name and lastMessageText
        VBox nameStatusVbox = new VBox();
        nameStatusVbox.getStyleClass().add("contactVBox");
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

    private ImageView createImageView(Contact contact) {
        Circle circle = new Circle(25);
        circle.setCenterX(25);
        circle.setCenterY(25);
        ImageView imageView = new ImageView();
        imageView.setClip(circle);
        if(contact.getProfilePicture().isEmpty()){
            //default profile picture
            imageView.setImage(application.loadImage(application.getDefaultProfilePictureString()));
        }
        else{
            try{imageView.setImage(application.loadImage(contact.getProfilePicture()));}
            catch(Exception exception){
                //default profile picture
                imageView.setImage(application.loadImage(application.getDefaultProfilePictureString()));
            }
        }

        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        imageView.getStyleClass().add("contactProfilePicture");

        return imageView;
    }

    private HBox createMessageHBox(Message message, boolean alignRight){
        HBox messageWrapper = new HBox();
        HBox messageHBox = new HBox();
        VBox messageVBox = new VBox();
        messageVBox.setSpacing(6.0);
        messageWrapper.setSpacing(3.0);

        //add image if image property is not empty
        if(!message.getImage().isEmpty()){
            Image image = new Image("file:C:\\Users\\morit\\IdeaProjects\\RoboRally\\src\\main\\resources\\soeldichat\\soeldichat\\img\\" + application.getFocusedContactNumber() +"\\" + message.getImage());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitHeight(image.getHeight() / (image.getHeight()/200));
            imageView.setFitWidth(image.getWidth() / (image.getWidth()/200));
            imageView.getStyleClass().add("messageImage");

            messageVBox.getChildren().add(imageView);
        }

        //add timestamp
        Label timeStampLabel = new Label(message.getTimeStamp().substring(11,16));
        timeStampLabel.getStyleClass().add("timeStamp");

        //add text
        Label textLabel = new Label(message.getText());
        textLabel.setWrapText(true);

        if(alignRight){
            messageHBox.getStyleClass().add("sentMessages");
            messageWrapper.setAlignment(Pos.CENTER_RIGHT);

            messageVBox.getChildren().add(textLabel);
            messageHBox.getChildren().add(messageVBox);
            messageHBox.setAlignment(Pos.BOTTOM_LEFT);
            messageWrapper.getChildren().add(messageHBox);
            messageWrapper.getChildren().add(timeStampLabel);
            messageWrapper.setSpacing(10.0);
        }
        else{
            messageHBox.getStyleClass().add("recievedMessages");
            messageWrapper.setAlignment(Pos.CENTER_LEFT);

            messageVBox.getChildren().add(textLabel);
            messageHBox.getChildren().add(messageVBox);
            messageHBox.setAlignment(Pos.BOTTOM_RIGHT);
            messageWrapper.getChildren().add(timeStampLabel);
            messageWrapper.getChildren().add(messageHBox);
            messageWrapper.setSpacing(4.0);
        }


        return messageWrapper;
    }
}

