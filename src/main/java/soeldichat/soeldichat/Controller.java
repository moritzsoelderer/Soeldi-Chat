package soeldichat.soeldichat;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Controller{
    @FXML
    private VBox imageDragVBox;
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
    private ImageView imageDragImageView;


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

        String text = sendTextArea.getText();
        //return if message prompt is empty
        if(text.isEmpty() && !imageDragVBox.getChildren().contains(imageDragImageView)){
            return;
        }

        String currentDateTime = java.time.LocalDateTime.now().toString();

        //add message
        addMessageToChat(new Message(application.getUserNumber(), application.getFocusedContactNumber(), text,imageDragImageView == null ? "" : imageDragImageView.getImage().getUrl(), currentDateTime), true);

        //clear message prompt
        sendTextArea.setText("");

        //clear imageViewVBox, reset style and reset imageDragImageView
        imageDragVBox.getChildren().remove(imageDragImageView);
        imageDragImageView = null;
        imageDragVBox.setStyle("-fx-border-color: -fx-light-accent");

        //scroll to message
        double vmax = currentChat.getVmax();
        currentChat.setVvalue(vmax);
    }

    protected void addMessageToChat(Message message, boolean alignRight){
        //Create Pane
        HBox messageWrapper = createMessageHBox(message, alignRight);
        messageContainer.getChildren().add(messageWrapper);

        //update messageList
        application.addMessageToChat(message);

        //update last message
        String text = message.getText().isEmpty() ? "~image" : message.getText(); //display ~image if text is empty
        ((Label)((VBox)focusedContactContainer.getChildren().get(1)).getChildren().getLast()).setText(text);
        //update timeStamp
        ((Label)(focusedContactContainer.getChildren().get(2))).setText(message.getTimeStamp().substring(11,16));
        //update contact position
        contactScrollpaneVBox.getChildren().remove(focusedContactContainer);
        contactScrollpaneVBox.getChildren().addFirst(focusedContactContainer);

        //update contact list and log contacts
        application.updateContactList(application.getFocusedContactNumber());
        application.logContacts();

        //save image if given
        application.saveImage(message.getImageUrl());
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
        HBox.setHgrow(nameStatusVbox, Priority.ALWAYS);
        nameStatusVbox.getStyleClass().add("contactVBox");
        contactContainer.getChildren().add(nameStatusVbox);

        //add Label for name
        Label name = new Label(contact.getFirstName() + " " + contact.getLastName());
        name.getStyleClass().add("contactName");
        nameStatusVbox.getChildren().add(name);

        //add label for lastMessageText and LastMessageTimeStamp
        Label lastMessageText = new Label(contact.getStatus());
        Label lastMessageTimeStampLabel = new Label();

        try{
            //try to fetch data
            lastMessageText.setText(contact.getMessageList().getLast().getText());
            lastMessageTimeStampLabel.setText(contact.getMessageList().getLast().getTimeStamp().substring(11,16));
        }
        catch(Exception ignored){}

        lastMessageText.getStyleClass().add("contactStatus");
        lastMessageTimeStampLabel.getStyleClass().add("contactLastMessageTimeStampLabel");
        nameStatusVbox.getChildren().add(lastMessageText);
        contactContainer.getChildren().add(lastMessageTimeStampLabel);

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
        if(message.getImageUrl() != null && !message.getImageUrl().isEmpty()){
            Image image = new Image(message.getImageUrl());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
            Rectangle clip = new Rectangle(200*(image.getWidth()/ image.getHeight()), imageView.getFitHeight());
            imageView.setClip(clip);
            clip.setArcHeight(20);
            clip.setArcWidth(20);
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

    @FXML
    protected void onDragEnteredImageLabel(DragEvent dragEvent){
        imageDragVBox.setStyle("-fx-border-color: -fx-accent-gradient");
    }

    @FXML
    protected void onDragExitedImageLabel(DragEvent dragEvent){
        if(!imageDragVBox.getChildren().contains(imageDragImageView)){
            imageDragVBox.setStyle("-fx-border-color: -fx-light-accent");
        }
    }

    @FXML
    protected void onDragOverImageLabel(DragEvent dragEvent){
        dragEvent.acceptTransferModes(TransferMode.LINK);
    }

    @FXML
    protected void onDragDroppedImageLabel(DragEvent dragEvent) {
        //only allow one image
        if(!imageDragVBox.getChildren().contains(imageDragImageView)){
            Image image = new Image("file:" + dragEvent.getDragboard().getFiles().getFirst().toString());
            imageDragImageView = new ImageView(image);
            imageDragImageView.setFitWidth(100);
            imageDragImageView.setPreserveRatio(true);
            imageDragVBox.getChildren().addFirst(imageDragImageView);
        }
    }

}