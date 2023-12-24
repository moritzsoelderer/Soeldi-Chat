package soeldichat.soeldichat;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

public class Controller {
    @FXML
    private HBox contactSearchResultHBox;
    @FXML
    private Button contactSearchButton;
    @FXML
    private Button sendButton;
    @FXML
    private StackPane chatMenuBarStackPane;
    @FXML
    private TextField contactSearchTextField;
    @FXML
    private Label imageDragVBoxLabel;
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
    private boolean isContactSearch = false;


    public void displayChat(List<Message> messageList) {
        //clearing chat
        messageContainer.getChildren().clear();

        //displaying Chat
        String lastMessageTimeStamp = "";

        for(Message message : messageList){
            if(!lastMessageTimeStamp.isEmpty()){
                int lastMessageDayInt = Integer.parseInt(lastMessageTimeStamp.substring(8,10));
                int messageDayInt = Integer.parseInt(message.getTimeStamp().substring(8,10));
                int dayDistance = messageDayInt - lastMessageDayInt;
                if(dayDistance > 0){
                    String dayDisplayText = ScFiles.getDateFormatted(message.getTimeStamp());
                    if(dayDistance == 1){
                        dayDisplayText = "yesterday";
                        if(java.time.LocalDate.now().toString().equals(ScFiles.getDate(message.getTimeStamp()))){
                            dayDisplayText = "today";
                        }
                    }
                    messageContainer.getChildren().add(ScPaneCreator.createDayTagStackpane(dayDisplayText));
                }
            }
            else{
                StackPane dayStackPane = ScPaneCreator.createDayTagStackpane(ScFiles.getDateFormatted(message.getTimeStamp()));
                messageContainer.getChildren().add(dayStackPane);
            }
            lastMessageTimeStamp = message.getTimeStamp();
            HBox messageWrapper = ScPaneCreator.createMessageHBox(message, message.getSender().equals(application.getUserNumber()), application);
            messageContainer.getChildren().add(messageWrapper);
            updateFocusedContactContainer(message);
        }
    }

    public void displayContacts(List<Contact> contactList) {
        //clear contactScrollPaneVBox
        contactScrollpaneVBox.getChildren().clear();

        contactList.forEach(contact -> {

            //create HBox and content for contact
            HBox newContactContainer = ScPaneCreator.createContactHBox(contact, this.application);
            newContactContainer.setOnMouseClicked(y -> onMouseClickedContactContainer(contact.getNumber(), newContactContainer));
            //add contact to contacts
            contactScrollpaneVBox.getChildren().add(newContactContainer);

        });
    }

    public void focusFirstContact(){
        focusedContactContainer = contactScrollpaneVBox.getChildren().getFirst() == null ? null :  (HBox) contactScrollpaneVBox.getChildren().getFirst();
    }

    protected void addMessageToChat(Message message, boolean alignRight) {
        messageContainer.getChildren().add(ScPaneCreator.createMessageHBox(message, alignRight, application));
        application.addMessageToChat(message);
        updateFocusedContactContainer(message);
        application.updateContactList(application.getFocusedContactNumber());
        application.logContacts();
    }

    public void updateChatMenuBar() {
        Contact focusedContact = Contact.getContactByNumber(application.getContactList(), application.getFocusedContactNumber());
        chatMenuBarContactName.setText(focusedContact.getFirstName() + " " + focusedContact.getLastName());
        chatMenuBarStatus.setText(focusedContact.getStatus());
        setupchatMenuBarProfilePicture(focusedContact);
    }

    private void updateFocusedContactContainer(Message message){
        //update last message
        String text = message.getText().isEmpty() ? "~image" : message.getText();//display ~image if text is empty
        ((Label) ((VBox) focusedContactContainer.getChildren().get(1)).getChildren().getLast()).setText(text);
        //update timeStamp
        ((Label) (focusedContactContainer.getChildren().get(2))).setText(message.getTimeStamp().substring(11, 16));
        //update contact position
        contactScrollpaneVBox.getChildren().remove(focusedContactContainer);
        contactScrollpaneVBox.getChildren().addFirst(focusedContactContainer);
    }

    public void setupchatMenuBarProfilePicture(Contact contact){
        chatMenuBarProfilePicture = ScPaneCreator.createContactProfilePictureImageView(contact, this.application);
        chatMenuBarStackPane.getChildren().add(chatMenuBarProfilePicture);
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

        if(!imageDragVBox.getChildren().contains(imageDragVBoxLabel)){ imageDragVBox.getChildren().add(imageDragVBoxLabel);}

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
        File media = dragEvent.getDragboard().getFiles().getFirst();
        String mediaFileExtension = ScFiles.getFileExtension(media);
        if(mediaFileExtension.equals(".mp4")){
            //TODO implement media view and media player for media support
        }
        else if(mediaFileExtension.equals(".jpg") || mediaFileExtension.equals(".png") && !imageDragVBox.getChildren().contains(imageDragImageView)){
            Image image = new Image("file:" + dragEvent.getDragboard().getFiles().getFirst().toString());
            imageDragImageView = new ImageView(image);
            imageDragImageView.setFitWidth(100);
            imageDragImageView.setPreserveRatio(true);
            imageDragVBox.getChildren().remove(imageDragVBoxLabel);
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
    @FXML
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

        if(isContactSearch){
            contactSearchTextField.setText("");
            displayContacts(application.getContactList());
            contactSearchResultHBox.getChildren().clear();
        }
        updateChatMenuBar();
    }

    @FXML
    protected void onKeyPressedSendTextArea(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            if(keyEvent.isShiftDown()){
                sendTextArea.appendText("\n");
            }
            else{
                sendButton.fire();
            }
        }
    }

    @FXML
    protected void onKeyPressedContactSearchTextArea(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && !keyEvent.isShiftDown()){
            if(keyEvent.isShiftDown()){
                contactSearchTextField.appendText("\n");
            }
            else{
                contactSearchButton.fire();
            }
        }
    }

    @FXML
    protected void onActionContactSearchButton(javafx.event.ActionEvent actionEvent) {
        List<Contact> contactList = Contact.filterContactListByName(application.getContactList(), contactSearchTextField.getText());
        contactSearchResultHBox.getChildren().clear();
        contactSearchResultHBox.getChildren().add(new Label(contactList.size() + " results"));
        displayContacts(contactList);
        isContactSearch = true;
    }

    public void setApplication(SoeldiChatApplication soeldiChatApplication) {
        this.application = soeldiChatApplication;
    }

}