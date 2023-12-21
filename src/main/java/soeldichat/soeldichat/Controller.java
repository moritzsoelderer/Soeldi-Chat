package soeldichat.soeldichat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class Controller {
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


    public void displayChat(List<Message> messageList) {
        //clearing chat
        messageContainer.getChildren().clear();

        //displaying Chat
        messageList.forEach(message -> {
            HBox messageWrapper = ScPaneCreator.createMessageHBox(message, message.getSender().equals(application.getUserNumber()));
            messageContainer.getChildren().add(messageWrapper);
        });
    }

    public void displayContacts(List<Contact> contactList) {
        contactList.forEach(contact -> {

            //create HBox and content for contact
            HBox newContactContainer = ScPaneCreator.createContactHBox(contact, this.application);
            newContactContainer.setOnMouseClicked(y -> onMouseClickedContactContainer(contact.getNumber(), newContactContainer));
            //add contact to contacts
            contactScrollpaneVBox.getChildren().add(newContactContainer);

        });
        //focus first contact
        focusedContactContainer = (HBox) contactScrollpaneVBox.getChildren().getFirst();
    }

    protected void addMessageToChat(Message message, boolean alignRight) {
        messageContainer.getChildren().add(ScPaneCreator.createMessageHBox(message, alignRight));
        application.addMessageToChat(message);
        updateFocusedContactContainer(message);
        application.updateContactList(application.getFocusedContactNumber());
        application.logContacts();
    }

    public void updateChatMenuBar() {
        Contact focusedContact = Contact.getContactByNumber(application.getContactList(), application.getFocusedContactNumber());
        chatMenuBarContactName.setText(focusedContact.getFirstName() + " " + focusedContact.getLastName());
        chatMenuBarStatus.setText(focusedContact.getStatus());

        if (focusedContact.getProfilePicture().isEmpty()) {
            //default profile picture
            chatMenuBarProfilePicture.setImage(application.loadImage(application.getDefaultProfilePictureString()));
        } else {
            try {
                chatMenuBarProfilePicture.setImage(application.loadImage(focusedContact.getProfilePicture()));
            } catch (Exception exception) {
                //default profile picture
                chatMenuBarProfilePicture.setImage(application.loadImage(application.getDefaultProfilePictureString()));
            }
        }
    }

    private void updateFocusedContactContainer(Message message){
        //update last message
        String text = message.getText().isEmpty() ? "~image" : message.getText(); //display ~image if text is empty
        ((Label) ((VBox) focusedContactContainer.getChildren().get(1)).getChildren().getLast()).setText(text);
        //update timeStamp
        ((Label) (focusedContactContainer.getChildren().get(2))).setText(message.getTimeStamp().substring(11, 16));
        //update contact position
        contactScrollpaneVBox.getChildren().remove(focusedContactContainer);
        contactScrollpaneVBox.getChildren().addFirst(focusedContactContainer);
    }

    @FXML
    protected void onSendButtonClicked() {

        String text = sendTextArea.getText();
        //return if message prompt is empty
        if (text.isEmpty() && !imageDragVBox.getChildren().contains(imageDragImageView)) {
            return;
        }

        String currentDateTime = java.time.LocalDateTime.now().toString();

        String newImageUrl = application.saveImage(imageDragImageView == null ? "" : imageDragImageView.getImage().getUrl());

        //add message
        addMessageToChat(new Message(application.getUserNumber(), application.getFocusedContactNumber(), text, newImageUrl, currentDateTime), true);

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
    @FXML
    protected void onDragEnteredImageLabel(DragEvent dragEvent) {
        imageDragVBox.setStyle("-fx-border-color: -fx-accent-gradient");
    }

    @FXML
    protected void onDragExitedImageLabel(DragEvent dragEvent) {
        if (!imageDragVBox.getChildren().contains(imageDragImageView)) {
            imageDragVBox.setStyle("-fx-border-color: -fx-light-accent");
        }
    }

    @FXML
    protected void onDragOverImageLabel(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.LINK);
    }

    @FXML
    protected void onDragDroppedImageLabel(DragEvent dragEvent) {
        //only allow one image
        if (!imageDragVBox.getChildren().contains(imageDragImageView)) {
            Image image = new Image("file:" + dragEvent.getDragboard().getFiles().getFirst().toString());
            imageDragImageView = new ImageView(image);
            imageDragImageView.setFitWidth(100);
            imageDragImageView.setPreserveRatio(true);
            imageDragVBox.getChildren().addFirst(imageDragImageView);
        }
    }

    @FXML
    protected void onMouseClickedImageLabel(MouseEvent mouseEvent) {
        try {
            Runtime.getRuntime().exec("explorer C:\\");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void onMouseClickedContactContainer(String focusedContactNumber, HBox newContactContainer){
        //store number of focused contact
        application.setFocusedContactNumber(focusedContactNumber);
        //store Hbox of focused contact
        focusedContactContainer.getStyleClass().clear();
        focusedContactContainer.getStyleClass().add("contact");
        focusedContactContainer = newContactContainer;
        focusedContactContainer.getStyleClass().clear();
        focusedContactContainer.getStyleClass().add("focusedContact");
        List<Message> messageList = Contact.getContactByNumber(application.getContactList(), focusedContactNumber).getMessageList();
        displayChat(messageList);

        updateChatMenuBar();
    }

    public void setApplication(SoeldiChatApplication soeldiChatApplication) {
        this.application = soeldiChatApplication;
    }
}