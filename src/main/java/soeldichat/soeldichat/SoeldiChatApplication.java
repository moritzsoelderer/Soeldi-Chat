package soeldichat.soeldichat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class SoeldiChatApplication extends Application{

    public static SoeldiChatApplication applicationInstance;
    public ScServerConnection scServerConnection;

    private final String userNumber = "0000000000";
    private String focusedContactNumber;
    private List<Contact> contactList = Collections.emptyList();
    private String defaultProfilePictureString = "default-profile-picture.png";


    private Controller controller;
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        applicationInstance = this;

        FXMLLoader fxmlLoader = new FXMLLoader(SoeldiChatApplication.class.getResource("soeldi-chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
        controller = fxmlLoader.getController();
        this.stage = stage;

        //give controller access to this instance of application
        controller.setApplication(this);

        //set stage and style of scene
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setMinWidth(650);
        stage.setMinHeight(400);
        stage.setTitle("SoeldiChat");
        stage.getIcons().add(new Image("file:src\\main\\resources\\soeldichat\\soeldichat\\img\\application-icon.jpg"));
        stage.setScene(scene);
        stage.show();

        this.contactList = loadContacts();

        if(!contactList.isEmpty()){
            //load and display contacts
            this.focusedContactNumber = contactList.getFirst().getNumber();

            //load and display messages (of first contact)
            contactList.forEach(x -> x.setMessageList(new ArrayList<>(loadMessages(x.getNumber()))));
            controller.displayContacts(contactList);
            controller.focusFirstContact();
            controller.setupchatMenuBarProfilePicture(contactList.getFirst());
            controller.displayChat(contactList.getFirst().getMessageList());
            controller.updateChatMenuBar();
        }


        //perform server connection
        scServerConnection = new ScServerConnection();
        scServerConnection.setApplication(this);
        if(!scServerConnection.connect()){
            addErrorMessage("could not connect to server");
        }
        else{
            scServerConnection.start();
        }
    }

    public static void main(String[] args) {
        launch();

        //logging
        applicationInstance.logContacts();
    }


    public void addMessageToChat(Message newMessage) {
        try{
            Contact.getContactByNumber(contactList, newMessage.determineChatPartnerNumber(this.userNumber)).getMessageList().add(newMessage);
            logMessage(newMessage);
            sendMessageToServer(newMessage);
        }
        catch(NullPointerException nullPointerException){
            System.out.println("no such contact in contactlist");
        }
    }

    public void updateContactList(String focusedContactNumber) {
        Contact focusedContact = Contact.getContactByNumber(getContactList(), focusedContactNumber);
        ArrayList<Contact> contactArrayList = new ArrayList<>(getContactList());
        contactArrayList.remove(focusedContact);
        contactArrayList.add(0, focusedContact);
        setContactList(contactArrayList);
    }

    private List<Contact> loadContacts() {
        File contactFile = new File("src/main/resources/soeldichat/soeldichat/chats/chats-info.json");
        return ScFiles.fetchContactsFromJson(contactFile);
    }

    private List<Message> loadMessages(String contactNumber) {
        File chatFile = new File("src/main/resources/soeldichat/soeldichat/chats/" + contactNumber + ".json");
        return ScFiles.fetchMessagesFromJson(chatFile);
    }

    public Image loadImage(String imageFileName) {
        return new Image("file:src\\main\\resources\\soeldichat\\soeldichat\\img\\profile-pictures\\" + imageFileName);
    }

    public void logContacts(){
        File chatFile = new File("src\\main\\resources\\soeldichat\\soeldichat\\chats\\chats-info.json");
        try {
            ScFiles.logToJson(chatFile, getContactList());
        } catch (NullPointerException nullPointerException) {
            System.out.println("contactList is null");
        }
    }

    private void logMessage(Message newMessage) {
        String chatPartnerNumber = newMessage.determineChatPartnerNumber(userNumber);
        File chatFile = new File("src\\main\\resources\\soeldichat\\soeldichat\\chats\\" + chatPartnerNumber + ".json");

        try {
            ArrayList<Message> messageList = new ArrayList<>(Contact.getContactByNumber(contactList, chatPartnerNumber).getMessageList());
            ScFiles.logToJson(chatFile, messageList);
        } catch (NullPointerException nullPointerException) {
            System.out.println("message logging failed");
        }
    }

    public String saveImage(String imageUrl) {
        if(imageUrl.isEmpty() || imageUrl == null){return "";}
        String currentDateTime = java.time.LocalDateTime.now().toString();
        String newImageUrl = "src\\main\\resources\\soeldichat\\soeldichat\\img\\" + focusedContactNumber + "\\" + new File(currentDateTime.replace(':', '-')) + ScFiles.getFileExtension(new File(imageUrl));
        return "file:" + ScFiles.saveImage(Path.of(imageUrl.substring(5)),Path.of(newImageUrl));
    }

    private void sendMessageToServer(Message message){
        if(scServerConnection != null){
            scServerConnection.sendMessage(message);
        }
    }

    public void addErrorMessage(String messageString){
        controller.displayErrorBanner(messageString);
    }

}

